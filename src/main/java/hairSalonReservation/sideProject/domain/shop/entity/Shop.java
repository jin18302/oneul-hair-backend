package hairSalonReservation.sideProject.domain.shop.entity;

import hairSalonReservation.sideProject.common.entity.BaseEntity;
import hairSalonReservation.sideProject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity @Table(name = "shops")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String businessId;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String introduction;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopTagMapper> shopTagMapperList = new ArrayList<>();


    @Column(nullable = true, columnDefinition = "TEXT")
    private String snsUriList;

    @Enumerated(EnumType.STRING)
    private ShopStatus shopStatus = ShopStatus.NOT_OPENED;

    private Long likeCount = 0L;

    private Shop(User user, String name, String businessId, String address, String phoneNumber, LocalTime openTime, LocalTime endTime,
                 String introduction, String snsUriList){
        this.user = user;
        this.name = name;
        this.businessId = businessId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.endTime = endTime;
        this.introduction = introduction;
        this.snsUriList = snsUriList;
    }

    public static Shop of(User user, String name, String businessId, String address, String phoneNumber, LocalTime openTime, LocalTime endTime,
                          String introduction, String snsUriList){
        return new Shop(user, name, businessId, address, phoneNumber, openTime, endTime, introduction, snsUriList);
    }

    public void update(String name,  String address, String phoneNumber, LocalTime openTime, LocalTime endTime,
                 String introduction, String snsUriList){
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.endTime = endTime;
        this.introduction = introduction;
        this.snsUriList = snsUriList;
    }

    public void delete(){
        this.setDeleted(true);
        this.setDeletedAt(LocalDateTime.now());
        this.shopStatus = ShopStatus.SHUTDOWN;
    }

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount(){
        this.likeCount--;
    }
}
