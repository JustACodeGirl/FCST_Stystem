 /**
 * @Author lyman.meng
 * @Version 1.0
 * @See
 * @Since [LearningHelp]/[API] 1.0
 */
package com.ovt.sale.fcst.api.response;

import com.ovt.sale.fcst.common.model.JsonDocument;


public class OvForcastResponse extends JsonDocument
{
    public static final JsonDocument SUCCESS = new OvForcastResponse();

    private static final String SERVICE_SALE_FORCAST = "SaleForcast";

    public OvForcastResponse()
    {
        super(SERVICE_SALE_FORCAST, JsonDocument.STATE_SUCCESS);
    }

    public OvForcastResponse(Object data)
    {
        super(SERVICE_SALE_FORCAST, data);
    }

    public OvForcastResponse(String errCode)
    {
        super(SERVICE_SALE_FORCAST, errCode);
    }

}
