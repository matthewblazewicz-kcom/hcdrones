package town.boom.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PossibleDelivery {

    private final Warehouse from;
    private final Order to;
    private final int productType;



}
