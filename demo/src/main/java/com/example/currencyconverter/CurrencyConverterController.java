package com.example.currencyconverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class CurrencyConverterController {

    @GetMapping("/")
    public String home() {
        return "index";  // Aceasta va căuta un fișier "index.html" în folderul "templates"
    }

    @GetMapping("/api/convert")
    public String convertCurrency(@RequestParam double usdAmount, @RequestParam int choice) {
        double eurRate = 0.92;
        double gbpRate = 0.82;
        double inrRate = 83.00;
        double result = 0.0;

        switch (choice) {
            case 1:
                result = usdAmount * eurRate;
                return "Amount in EUR: " + result;
            case 2:
                result = usdAmount * gbpRate;
                return "Amount in GBP: " + result;
            case 3:
                result = usdAmount * inrRate;
                return "Amount in INR: " + result;
            default:
                return "Invalid choice";
        }
    }
}

