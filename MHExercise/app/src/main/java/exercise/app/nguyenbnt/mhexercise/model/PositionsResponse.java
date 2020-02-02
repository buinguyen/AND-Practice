package exercise.app.nguyenbnt.mhexercise.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionsResponse {

    @JsonProperty("positions")
    List<Position> posList;

    @JsonProperty("positions")
    public List<Position> getPosList() {
        return posList;
    }
}