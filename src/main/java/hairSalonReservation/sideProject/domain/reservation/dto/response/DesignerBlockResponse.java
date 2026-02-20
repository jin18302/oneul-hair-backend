package hairSalonReservation.sideProject.domain.reservation.dto.response;

import hairSalonReservation.sideProject.domain.designer.entity.Designer;

import java.util.List;

public record DesignerBlockResponse(Long designerId, List<ScheduleBlockResponse> blockResponseList) {

    public static DesignerBlockResponse of(Designer designer, List<ScheduleBlockResponse> blockResponseList){
        return new DesignerBlockResponse(designer.getId(), blockResponseList);
    }
}
