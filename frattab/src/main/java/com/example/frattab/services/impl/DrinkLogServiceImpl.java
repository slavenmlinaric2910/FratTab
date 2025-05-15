package com.example.frattab.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.frattab.dto.DrinkLogDto;
import com.example.frattab.dto.DrinkQtyDto;
import com.example.frattab.dto.DrinkSelectionDto;
import com.example.frattab.models.DrinkLog;
import com.example.frattab.models.DrinkQty;
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
        drinkLog.setMemberId(member.getId());

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
     *  - Member info
     *  - List of all available drinks
     *  - An empty DrinkLogDto pre‑populated with DrinkQtyDto entries (qty=0)
     *    so Thymeleaf can bind inputs by index.
     * 
     * @param memberId the ID of the Member for whom we are selecting drinks
     * @return a DTO for rendering the selection form
     */
    @Override
    public DrinkSelectionDto prepareSelection(Long memberId) {
        // Fetch member and available drinks
        var member = membersService.getMemberById(memberId);
        var drinks = drinksService.getAllDrinks();

        // Initialize the form‐binding DTO
        DrinkLogDto drinkLogDto = new DrinkLogDto();
        drinkLogDto.setMemberId(memberId);

        // Pre-populate one DrinkQtyDto per drink, qty default to 0
        for (var drink : drinks) {
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
}
