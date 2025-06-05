package com.example.frattab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.frattab.services.BillingService;

@SpringBootApplication
public class FrattabApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrattabApplication.class, args);
	}

	  @Bean
    public CommandLineRunner testBilling(BillingService billingService) {
        return args -> {
            // If you pass “run-bill” as a program argument, it will run billing immediately:
            if (args.length > 0 && "run-bill".equalsIgnoreCase(args[0])) {
                System.out.println(">>> Running a one-off billing run for testing <<<");
                var bills = billingService.runQuarterlyBilling();
                bills.forEach(dto -> {
                    System.out.printf(
                        "[TEST] Member %d – %s %s owes €%.2f [%s – %s]%n",
                        dto.getMemberId(),
                        dto.getFirstName(),
                        dto.getLastName(),
                        dto.getAmountOwed(),
                        dto.getPeriodStart(),
                        dto.getPeriodEnd()
                    );
                });
            }
        };
    }
}
