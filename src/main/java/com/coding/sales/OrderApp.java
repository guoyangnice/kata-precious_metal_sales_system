package com.coding.sales;

import com.coding.sales.input.OrderCommand;
import com.coding.sales.member.MemberRepository;
import com.coding.sales.order.Order;
import com.coding.sales.order.OrderFactory;
import com.coding.sales.output.OrderRepresentation;
import com.coding.sales.product.ProductRepository;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("参数不正确。参数1为销售订单的JSON文件名，参数2为待打印销售凭证的文本文件名.");
        }

        String jsonFileName = args[0];
        String txtFileName = args[1];

        String orderCommand = FileUtils.readFromFile(jsonFileName);
        OrderApp app = new OrderApp();
        String result = app.checkout(orderCommand);
        FileUtils.writeToFile(result, txtFileName);
    }

    public String checkout(String orderCommand) {
        OrderCommand command = OrderCommand.from(orderCommand);
        OrderRepresentation result = checkout(command);
        
        return result.toString();
    }

    OrderRepresentation checkout(OrderCommand command) {
        MemberRepository memberRepository = new MemberRepository();
        ProductRepository productRepository = new ProductRepository();
        OrderFactory factory = new OrderFactory(memberRepository, productRepository);
        Order order = factory.createOrder(command);
        order.checkout();

        OrderRepresentation representation = new OrderRepresentation(order);

        return representation;
    }
}
