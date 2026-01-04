package hairSalonReservation.sideProject.domain.shop.repository;

import hairSalonReservation.sideProject.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByIdAndIsDeletedFalse(Long shopId);

    Optional<Shop> findByUserId(Long userId);
}
