package com.example.frattab.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkQtyDto;
import com.example.frattab.dto.DrinkSelectionDto;
import com.example.frattab.dto.QuickConsumptionDto;
import com.example.frattab.dto.ResponseDto;
import com.example.frattab.models.Drink;
import com.example.frattab.models.DrinkLog;
import com.example.frattab.models.DrinkQty;
import com.example.frattab.models.Member;
import com.example.frattab.repositories.DrinkLogRepository;
import com.example.frattab.repositories.DrinkRepository;
import com.example.frattab.repositories.MemberRepository;
import com.example.frattab.services.DrinkLogService;
import com.example.frattab.services.DrinksService;
import com.example.frattab.services.MembersService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of DrinkLogService.
 * 
 * Responsible for preparing the data needed to render the drink-selection page
 * and persisting DrinkLog entries along with their line-items (DrinkQty).
 */
@Service
@RequiredArgsConstructor
public class DrinkLogServiceImpl implements DrinkLogService {

    private final MembersService membersService;
    private final DrinksService drinksService;
    private final DrinkLogRepository drinkLogRepository;
    private final DrinkRepository drinkRepository;
    private final MemberRepository memberRepository;

    /**
     * Persists a new DrinkLog and its associated DrinkQty line-items.
     * 
     * @param drinkLogDto contains the member ID and a list of DrinkQtyDto
     *                    indicating which drinks and quantities to log.
     */
    @Override
    @Transactional
    public void logDrinks(DrinkLogDto drinkLogDto) {
        // Fetch and validate the Member; throw if not found
        var member = memberRepository.findById(drinkLogDto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // Create a new DrinkLog entity and associate with the member
        DrinkLog drinkLog = new DrinkLog();
        drinkLog.setMember(memberRepository.findById(member.getId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found")));

        double totalLogAmount = 0.0;

        // Iterate over each requested drink quantity
        for (DrinkQtyDto drinkQtyDto : drinkLogDto.getDrinkQuantities()) {
            int quantity = drinkQtyDto.getQty();
            // Skip any entries with zero or negative quantity
            if (quantity <= 0) {
                continue;
            }

            // Load the Drink entity; throw if not found
            var drink = drinkRepository.findById(drinkQtyDto.getDrinkId())
                    .orElseThrow(() -> new EntityNotFoundException("Drink not found"));
            // Create a new DrinkQty (join entity) for this line-item
            DrinkQty drinkQty = new DrinkQty();
            drinkQty.setDrink(drink);
            drinkQty.setQty(quantity);
            // Compute the line‑item total and assign it
            double lineTotal = drink.getPrice() * quantity;
            drinkQty.setTotal(lineTotal);


            // Link the line‑item to the parent log (handles setting both sides)
            drinkLog.addDrinkQty(drinkQty);
            // Accumulate into the log’s running total
            totalLogAmount += lineTotal;
        }

        // Set the overall total on the DrinkLog before saving
        drinkLog.setTotal(totalLogAmount);

        // Persist the DrinkLog along with its cascade‑persisted DrinkQty items
        drinkLogRepository.save(drinkLog);
    }

    /**
     * Prepares the data needed for the drink-selection page.
     * 
     * Builds a DrinkSelectionDto containing:
     * - Member info
     * - List of all available drinks
     * - An empty DrinkLogDto pre‑populated with DrinkQtyDto entries (qty=0)
     * so Thymeleaf can bind inputs by index.
     * 
     * @param memberId the ID of the Member for whom we are selecting drinks
     * @return a DTO for rendering the selection form
     */
    @Override
    public DrinkSelectionDto prepareSelection(Long memberId) {
        // Fetch member and available drinks
        Member member = membersService.getMemberById(memberId);
        List<Drink> drinks = drinksService.getAllDrinks();

        // Initialize the form‐binding DTO
        DrinkLogDto drinkLogDto = new DrinkLogDto();
        drinkLogDto.setMemberId(memberId);

        // Pre-populate one DrinkQtyDto per drink, qty default to 0
        for (Drink drink : drinks) {
            double roundedPrice = BigDecimal.valueOf(drink.getPrice())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
            drink.setPrice(roundedPrice);
            DrinkQtyDto drinkQtyDto = new DrinkQtyDto();
            drinkQtyDto.setDrinkId(drink.getId());
            drinkQtyDto.setQty(0);

            drinkLogDto.getDrinkQuantities().add(drinkQtyDto);

        }

        // Wrap into a selection DTO
        DrinkSelectionDto selection = new DrinkSelectionDto();
        selection.setMember(member);
        selection.setDrinks(drinks);
        selection.setDrinkLogDto(drinkLogDto);
        return selection;
    }

    @Override
    @Transactional
    public ResponseDto quickDrinkLog(QuickConsumptionDto dto) {
        ResponseDto response = new ResponseDto();

        DrinkLog drinkLog = new DrinkLog();
        drinkLog.setMember(memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found")));
        Drink drink = drinkRepository.findById(dto.getDrinkId())
                .orElseThrow(() -> new EntityNotFoundException("Drink not found"));
        DrinkQty drinkQty = new DrinkQty();
        drinkQty.setDrink(drink);
        drinkQty.setQty(dto.getQuantity());
        drinkQty.setTotal(BigDecimal.valueOf(drink.getPrice() * dto.getQuantity())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue());
        drinkLog.addDrinkQty(drinkQty);
        drinkLog.setTotal(drinkQty.getTotal());
        drinkLogRepository.save(drinkLog);
        drinksService.updateDrinkQty(dto.getDrinkId(), dto.getQuantity());

        return response;
    }

    @Override
    public Page<DrinkLog> getRecentDrinkLogs(int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        System.out.println("Pageable: " + drinkLogRepository.findAll(pageable));
        return drinkLogRepository.findAll(pageable);
    }
}