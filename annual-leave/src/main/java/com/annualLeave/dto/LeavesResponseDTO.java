// LeavesResponseDTO.java
package com.annualLeave.dto;

import lombok.Data;

@Data
public class LeavesResponseDTO extends LeavesRequestDTO {
	
	
    private Boolean admin1;
    private Boolean admin2;
    private Boolean admin3;
    private Boolean admin4;
    private Boolean isapprove;
    private Boolean isnotapprove;
}
