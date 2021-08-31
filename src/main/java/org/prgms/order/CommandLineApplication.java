package org.prgms.order;

import org.prgms.order.customer.repository.FileCustomerRepository;
import org.prgms.order.customer.service.CustomerService;
import org.prgms.order.io.Input;
import org.prgms.order.io.Output;
import org.prgms.order.voucher.entity.Voucher;
import org.prgms.order.voucher.entity.VoucherCreateStretage;
import org.prgms.order.voucher.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.UUID;
import java.util.regex.Pattern;

public class CommandLineApplication implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(CommandLineApplication.class);
    private Input input;
    private Output output;

    private VoucherService voucherService;
    private Voucher voucher;
    private VoucherCreateStretage voucherCreateStretage;

    private CustomerService customerService;


    public CommandLineApplication(Input input, Output output) {
        this.input = input;
        this.output = output;
        voucherCreateStretage = new VoucherCreateStretage();
    }

    @Override
    public void run() {
        var applicationContext = new AnnotationConfigApplicationContext(AppConfiguration.class);
        voucherService = applicationContext.getBean(VoucherService.class);
        customerService = applicationContext.getBean(CustomerService.class);


        while(true){
            output.mainMenu();
            String inputString = input.input(":: ");

            switch (inputString) {
                case "create" -> createVoucher();
                case "list" -> printVoucherList();
                case "blackList" -> printBlackList();
                case "exit" -> {
                    System.out.println("=== Exit Program ===\n");
                    return;
                }
                default -> System.out.println("= RETRY =");
            }
        }

    }

    private void printBlackList() {
        System.out.println("\n=== BlackList ===");
        System.out.println("            CustomerId                name         email");

        customerService.findAllBlackList().forEach((customer) ->
                System.out.println(customer.getInfo())
        );
    }


    private void printVoucherList() {
        System.out.println("\n=== Vouchers ===");
        System.out.println("         TYPE              AMOUNT");

        voucherService.findAllVoucher().forEach((voucher) ->
                System.out.println(voucher.getVoucherInfo())
        );
    }


    private void createVoucher() {
        output.selectVoucher();

        String inputVoucherString = input.input(":: ");
        if(isWrongType(inputVoucherString)){
            System.out.println("=  Insert Correct Type. PLEASE RETRY =");
            return;
        }
        String inputAmount = input.input("Insert Discount Amount :: ");

        if(isNotDigit(inputAmount)) {
            System.out.println("=  Insert Correct Amount. PLEASE RETRY =");
            return;
        }


        voucher = voucherService.insert(voucherCreateStretage.createVoucher(inputVoucherString, UUID.randomUUID(), Long.parseLong(inputAmount)));
        System.out.println("SUCCESS >>> "+voucher.getVoucherInfo());
    }

    private boolean isWrongType(String inputVoucherString) {
        return !(inputVoucherString.contains("Fixed") || inputVoucherString.contains("Percent"));
    }


    private boolean isNotDigit(String input) {
        String pattern = "^[0-9]*$";
        return !(Pattern.matches(pattern,input) && !input.isEmpty());
    }


}
