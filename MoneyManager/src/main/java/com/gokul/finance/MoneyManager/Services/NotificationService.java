package com.gokul.finance.MoneyManager.Services;

import com.gokul.finance.MoneyManager.Dto.ExpensDto;
import com.gokul.finance.MoneyManager.Entities.ProfileEntity;
import com.gokul.finance.MoneyManager.Repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final ProfileRepository profileRepository;
    private final ExpenseService expenseService;
    private final EmailService emailService;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

   // @Scheduled(cron = "0 * * * * *", zone = "IST")
    @Scheduled(cron = "0 0 21 * * *", zone = "IST")
    public void sendDailyIncomeAndExpensesReminder() {
     log.info("Job started: sendDailyIncomeAndExpensesReminder()");
     List<ProfileEntity> profileEntities = profileRepository.findAll();
     for(ProfileEntity profile:profileEntities) {
         String body =
                 "Hi " + profile.getFullName() + ",<br><br>"
                         + "Hope you’re having a great day! 😊<br><br>"
                         + "This is a gentle reminder to update your <b>today’s income and expenses</b> "
                         + "in <b>Money Manager</b>.<br>"
                         + "Keeping your records up to date helps you stay in control of your finances "
                         + "and track your goals more effectively.<br><br>"
                         + "<a href=\"" + frontendUrl + "\" "
                         + "style=\"background-color:#4CAF50;"
                         + "color:white;"
                         + "padding:12px 20px;"
                         + "text-decoration:none;"
                         + "border-radius:6px;"
                         + "font-weight:bold;"
                         + "display:inline-block;\">"
                         + "Update Now"
                         + "</a><br><br>"
                         + "If you’ve already updated today’s entries, feel free to ignore this message 👍<br><br>"
                         + "Stay consistent. Stay in control. 💰📊<br><br>"
                         + "Best regards,<br>"
                         + "<b>Money Manager Team</b>";
         emailService.sendEmail(profile.getEmail(),"Dayily Reminder: Add you income and expense",body);
     }
    }

//    @Scheduled(cron = "0 0 20 * * *", zone = "IST")
//    public void sendDailyExpenseSummary() {
//        log.info("Job started: sendDailyExpenseSummary()");
//        List<ProfileEntity>  profiles = profileRepository.findAll();
//
//        for (ProfileEntity profile:profiles) {
//            List<ExpensDto> todayExpense = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
//            if (!todayExpense.isEmpty() ) {
//
//            }
//        }
//    }


}
