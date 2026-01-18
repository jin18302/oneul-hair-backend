package hairSalonReservation.sideProject.domain.serviceMenu.dto.response;

import hairSalonReservation.sideProject.domain.serviceMenu.entity.ServiceMenu;

public record ServiceMenuResponse(
        Long id,
        String category,
        String name,
        Integer price,
        String introduction
) {

    public static ServiceMenuResponse from(ServiceMenu serviceMenu){
        return new ServiceMenuResponse(
                serviceMenu.getId(),
                serviceMenu.getCategory().name(),
                serviceMenu.getName(),
                serviceMenu.getPrice(),
                serviceMenu.getIntroduction()
        );
    }
}
