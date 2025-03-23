package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.*;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.usecase.validations.DishValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

   @Mock
   private IDishPersistencePort dishPersistence;

   @Mock
   private DishValidations dishValidations;

   @Mock
   private TokenValidations tokenValidations;

   @InjectMocks
   private DishUseCase dishUseCase;


   private Dish createValidDish() {
      Restaurant restaurant = new Restaurant(1L, "Test Restaurant", "Address", 100L, "+573001234567", "logo.jpg", "123456789");
      Category category = new Category(1L, "Main", "Main dishes");
      return new Dish(1L, "Pasta", category, "Delicious pasta", 15000, restaurant, "pasta.jpg", true);
   }


   @Test
   void createDish_ShouldSaveValidDish() {


      Dish dish = createValidDish();
      String token = "valid_token";

      when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
      doNothing().when(tokenValidations).validateTokenAndOwnership(anyString(), eq(1L));
      when(dishPersistence.saveDish(dish)).thenReturn(dish);


      Dish result = dishUseCase.createDish(dish, token);


      assertThat(result).isEqualTo(dish);
      verify(dishValidations).validateDish(dish);
   }



   @Test
   void getAllDishesByRestaurant_ShouldReturnPaginatedResults() {

      List<Dish> dishes = List.of(createValidDish());
      Pagination<Dish> expectedPagination = new Pagination<>(dishes, 0, 1, 1L);

      when(dishPersistence.findAllDishesByRestaurant(1L, 1L, 0, 10, "name"))
              .thenReturn(expectedPagination);


      Pagination<Dish> result = dishUseCase.getAllDishesByRestaurant(1L, 1L, 0, 10, "name");


      assertThat(result.getItems()).hasSize(1);
      assertThat(result.getTotalItems()).isEqualTo(1L);
   }


   @Test
   void createDish_ShouldThrowForInvalidPrice() {

      Dish dish = createValidDish();
      dish.setPrice(-100); // Invalid price
      String token = "token";

      doThrow(InvalidPriceException.class).when(dishValidations).validateDish(dish);


      assertThatThrownBy(() -> dishUseCase.createDish(dish, token))
              .isInstanceOf(InvalidPriceException.class);
   }

   @Test
   void createDish_ShouldThrowWhenRestaurantIsNull() {

      Dish dish = createValidDish();
      dish.setRestaurant(null);
      String token = "token";

      when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");


      assertThatThrownBy(() -> dishUseCase.createDish(dish, token))
              .isInstanceOf(RestaurantNotFoundException.class);
   }

   @Test
   void createDish_ShouldThrowWhenCategoryIsNull() {

      Dish dish = createValidDish();
      dish.setCategory(null);
      String token = "token";

      when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");


      assertThatThrownBy(() -> dishUseCase.createDish(dish, token))
              .isInstanceOf(CategoryNotFoundException.class);
   }


   @Test
   void updateDish_ShouldUpdateDescriptionAndPrice() {

      Long dishId = 1L;
      Dish existingDish = createValidDish();
      Dish updates = new Dish();
      updates.setDescription("New description");
      updates.setPrice(20000);
      String token = "token";

      when(dishPersistence.findById(dishId)).thenReturn(existingDish);
      when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
      when(dishPersistence.saveDish(existingDish)).thenReturn(existingDish);


      Dish result = dishUseCase.updateDish(dishId, updates, token);


      assertThat(result.getDescription()).isEqualTo("New description");
      assertThat(result.getPrice()).isEqualTo(20000);
      verify(dishValidations).validateDishUpdate(existingDish);
   }

   @Test
   void updateDish_ShouldValidateOwnership() {

      Long dishId = 1L;
      Dish existingDish = createValidDish();
      String token = "token";

      when(dishPersistence.findById(dishId)).thenReturn(existingDish);
      when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");


      dishUseCase.updateDish(dishId, new Dish(), token);


      verify(tokenValidations).validateTokenAndOwnership("clean_token", existingDish.getRestaurant().getId());
   }


   @Test
   void activeDish_ShouldToggleActiveStatus() {

      Long dishId = 1L;
      Dish dish = createValidDish();
      dish.setActive(false);
      String token = "token";

      when(dishPersistence.findById(dishId)).thenReturn(dish);
      when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
      when(dishPersistence.saveDish(dish)).thenReturn(dish);


      Dish result = dishUseCase.activeDish(dishId, true, token);


      assertThat(result.getActive()).isTrue();
   }

   @Test
   void activeDish_ShouldThrowWhenNotOwner() {

      Long dishId = 1L;
      Dish dish = createValidDish();
      String token = "token";

      when(dishPersistence.findById(dishId)).thenReturn(dish);
      when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
      doThrow(SecurityException.class).when(tokenValidations).validateTokenAndOwnership(anyString(), anyLong());


      assertThatThrownBy(() -> dishUseCase.activeDish(dishId, true, token))
              .isInstanceOf(SecurityException.class);
   }


   @Test
   void getAllDishes_ShouldHandleInvalidParameters() {

      when(dishPersistence.findAllDishesByRestaurant(anyLong(), anyLong(), anyInt(), anyInt(), anyString()))
              .thenReturn(new Pagination<>(List.of(), 0, 0, 0L));

      Pagination<Dish> result = dishUseCase.getAllDishesByRestaurant(-1L, -1L, -1, -1, "invalid");


      assertThat(result.getItems()).isEmpty();
   }


}
