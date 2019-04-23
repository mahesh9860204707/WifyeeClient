package com.wifyee.greenfields.models;

public class OrderModel {
    private String OrderId,OrderAmount,OrderDate,MerchantId,EmpName,TaskId,MerchantType;

    public OrderModel(String orderId, String orderAmount, String orderDate, String merchantId,
                      String empName, String taskId, String merchantType) {
        OrderId = orderId;
        OrderAmount = orderAmount;
        OrderDate = orderDate;
        MerchantId = merchantId;
        EmpName = empName;
        TaskId = taskId;
        MerchantType = merchantType;
    }

    public String getOrderId() {
        return OrderId;
    }

    public String getOrderAmount() {
        return OrderAmount;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public String getEmpName() {
        return EmpName;
    }

    public String getTaskId() {
        return TaskId;
    }

    public String getMerchantType() {
        return MerchantType;
    }
}
