package hairSalonReservation.sideProject.common.public_data.dto.response;

import java.util.ArrayList;
import java.util.List;

public record ReadAddrApiResponse(List<ReadAddrResponse> result) {

    public ReadAddrApiResponse(List<ReadAddrResponse> result){

        List<ReadAddrResponse> resultList = new ArrayList<>();

        if(result != null){
            resultList.add(0, ReadAddrResponse.from("0", "전체", "전체"));
            //이거를 해당 지역의 값으로 넣으면 되잖아 0대산에
            resultList.addAll(result);
        }

        this.result = resultList;
    }
}
