package hairSalonReservation.sideProject.domain.designer.entity;

import hairSalonReservation.sideProject.common.entity.BaseEntity;
import hairSalonReservation.sideProject.domain.shop.entity.Shop;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity @Table(name = "designers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Designer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Shop shop;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String introduction;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String snsUrlList;

    @Column(nullable = false,  columnDefinition = "TEXT")
    private String timeSlotList;

    private Designer(Shop shop, String name, String profileImage, String introduction,
                      String snsUriList, String timeSlotList
    ){
        this.shop = shop;
        this.name = name;
        this.profileImage = profileImage;
        this.introduction = introduction;
        this.snsUrlList = snsUriList;
        this.timeSlotList = timeSlotList;
    }

    public static Designer of(Shop shop, String name, String profileImage, String introduction,
                             String snsUriList, String timeSlotList
    ){
        return new Designer(shop, name, profileImage, introduction, snsUriList, timeSlotList);



    }

    public void update(String name, String profileImage, String introduction, String snsUriList){
        this.name = name;
        this.profileImage = profileImage;
        this.introduction = introduction;
        this.snsUrlList = snsUriList;
    }

    public void delete(){
        this.setDeleted(true);
        this.setDeletedAt(LocalDateTime.now());
    }
}
