# 같이먹자

같이먹자 백엔드 저장소입니다. 
대학교 기숙사 및 인근 지역 사용자가 식사 공동 구매 파티를 생성·참여하고, 실시간 채팅으로 소통하며, 카카오 인증 기반으로 안전하게 소통할 수 있도록 지원합니다.

## 목차
- [주요 기능](#주요-기능)
- [시스템 개요](#시스템-개요)
- [기술 스택](#기술-스택)
- [디렉터리 구조](#디렉터리-구조)
- [환경 변수](#환경-변수)
- [도메인별 설명](#도메인별-설명)
- [실시간 채팅](#실시간-채팅)
- [로깅 & 모니터링](#로깅--모니터링)

## 주요 기능
- **카카오 OAuth 2.0** 기반 소셜 로그인 및 신규/기존 회원 분기 처리
- **JWT 토큰** 기반 무상태 인증 (Access/Refresh, Temp 토큰)
- **공동 구매 관리**: 생성, 수정, 마감, 참여, 강퇴 등 전 lifecycle 지원
- **실시간 채팅**: STOMP/WebSocket 기반 파티 단위 채팅 룸
- **Swagger UI**를 통한 API 문서화
- **Dev 토큰 발급 엔드포인트**로 로컬 개발 가속화

## 시스템 개요
- `api` 계층은 REST 컨트롤러로 구성돼 있으며, 모든 보호된 라우트는 JWT 필터를 통과합니다.
- `domain/*` 모듈은 파티, 유저, 채팅의 Bounded Context로 나뉘고 서비스 레이어에서 JPA 리포지토리를 통해 트랜잭션을 수행합니다.
- `security` 모듈은 Kakao OAuth, JWT 발급/검증, STOMP 인터셉터, Spring Security 필터체인을 제공합니다.
- `global/config`의 Swagger 설정으로 런타임 API 문서가 자동 생성됩니다.

## 기술 스택
- **Language**: Java 21
- **Framework**: Spring Boot 3.5, Spring Web, Spring Security, Spring Data JPA
- **Auth**: Spring Security OAuth2 Client, JWT (jjwt)
- **Realtime**: Spring WebSocket, STOMP
- **DB**: H2 in-memory
- **Docs**: springdoc-openapi
- **Build**: Gradle 8 (Wrapper)

## 디렉터리 구조
```
src/main/java/com/css/gachimeokja
├─ dev/controller           # 개발자 보조 API (Dev 토큰)
├─ domain
│  ├─ chat                  # 채팅 DTO/Entity/Service/WebSocket 설정
│  ├─ party                 # 공동구매 CRUD 및 참여 로직
│  └─ user                  # 사용자 가입/조회
├─ global/config            # Swagger 등 글로벌 설정
└─ security                 # Kakao OAuth, JWT, 필터, STOMP 핸들러
src/main/resources/
└─ db_schema.dbml           # DB 다이어그램
```

## 환경 변수
| 키 | 설명 | 비고 |
| --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | 실행 프로파일 (`local`, `dev`, `prod`) | 기본은 `default` |
| `SPRING_DATASOURCE_URL` | MySQL JDBC URL | 예: `jdbc:mysql://localhost:3306/gachimeokja` |
| `SPRING_DATASOURCE_USERNAME` | DB 계정 |  |
| `SPRING_DATASOURCE_PASSWORD` | DB 비밀번호 |  |
| `JWT_SECRET_KEY` | JWT 서명 키 | 최소 32자 |
| `JWT_ACCESS_TOKEN_EXPIRE_TIME` | Access Token TTL(ms) | 기본 30분 |
| `JWT_REFRESH_TOKEN_EXPIRE_TIME` | Refresh Token TTL(ms) | 기본 7일 |
| `KAKAO_CLIENT_ID` | Kakao REST API 키 | Kakao Developers |
| `KAKAO_REDIRECT_URI` | 프론트 콜백 URL | 로컬: `http://localhost:3000/` |
| `KAKAO_TOKEN_URI` | Kakao 토큰 발급 URL | 기본값 사용 가능 |
| `KAKAO_USER_INFO_URI` | Kakao 사용자 정보 URL | 기본값 사용 가능 |

## 도메인별 설명
- **인증/인가 (`security`)**
  - `/api/v1/auth/kakao/callback`에서 카카오 인가 코드를 받아 Access/Refresh/Temp 토큰을 발급합니다.
  - `JwtAuthenticationFilter`가 모든 요청 헤더의 `Authorization: Bearer <token>`을 검증하고 `AuthenticationPrincipal`로 `socialId`를 제공합니다.
  - `/api/v1/dev/token`은 개발 편의를 위한 임시 토큰 발급 API이며 운영 배포 시 비활성화를 권장합니다.
- **사용자 (`domain.user`)**
  - `UserSignUpRequest`로 신규 회원 정보를 받고, 대학(`University` enum)과 닉네임을 저장합니다.
  - `/api/v1/users/universities`로 지원 대학 목록을 제공합니다.
- **공동구매 (`domain.party`)**
  - `PartyController`에서 생성/수정/마감/참여/강퇴 API를 제공합니다.
  - 참여 성공 시 해당 파티 채팅방 ID를 반환하여 바로 채팅에 합류할 수 있습니다.
- **채팅 (`domain.chat`)**
  - `ChatMessage` 엔티티로 파티별 채팅 로그를 영속화하며, `ChatService`가 메시지 저장과 브로드캐스트를 동시에 처리합니다.
  - 메시지 타입(`ENTER`, `TALK`, `QUIT`)을 지원합니다.

## 실시간 채팅
- STOMP 엔드포인트: `ws` (`ws://localhost:8080/ws`)
- Publish 경로: `/pub/chat.message`
- Subscribe 경로: `/sub/chat.room.{partyId}`
- `StompHandler`가 CONNECT 프레임의 JWT를 검증하므로, 헤더 `Authorization: Bearer <token>`을 반드시 포함해야 함

## 로깅 & 모니터링
- `src/main/resources/application.yml`에서 Logback 패턴 및 수준을 정의하며, 기본 로그 파일은 `logs/gachimeokja.log`입니다.