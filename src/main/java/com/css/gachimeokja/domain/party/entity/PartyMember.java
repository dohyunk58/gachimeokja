package com.css.gachimeokja.domain.party.entity;

import com.css.gachimeokja.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "party_members")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 공동구매 참여자

    @Column(name = "order_amount")
    private Integer orderAmount; // 주문한 금액

    @Enumerated(EnumType.STRING)
    private MemberStatus status; // APPROVED, BANNED

    @Builder
    public PartyMember(Party party, User user, Integer orderAmount) {
        this.party = party;
        this.user = user;
        this.orderAmount = orderAmount;
        this.status = MemberStatus.APPROVED;
    }

    public enum MemberStatus {
        APPROVED,
        BANNED
    }
}