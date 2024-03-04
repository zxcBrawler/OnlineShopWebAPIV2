package com.example.shop.controllers

import com.example.shop.models.*
import com.example.shop.models.dto.Message
import com.example.shop.models.dto.OrderDTO
import com.example.shop.repositories.*
import com.example.shop.repositories.AddressRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Controller for managing user orders.
 *
 * @see createUserOrder method that creates new order when user has paid their order
 *
 * @property userOrderRepository The repository for UserOrder entities.
 * @property statusOrderRepository The repository for StatusOrder entities.
 * @property userRepository The repository for User entities.
 * @property orderRepository The repository for Order entities.
 * @property userCardRepository The repository for UserCard entities.
 * @property orderCompositionRepository The repository for OrderComposition entities.
 * @property addressesRepository The repository for Address entities.
 * @property shopAddressesRepository The repository for ShopAddresses entities.
 * @property typeDeliveryRepository The repository for TypeDelivery entities.
 * @property cartRepository The repository for Cart entities.
 * @property deliveryInfoRepository The repository for DeliveryInfo entities.
 * @property clothesRepository The repository for Clothes entities.
 * @property colorRepository The repository for Color entities.
 * @property sizeClothesRepository The repository for SizeClothes entities.
 */
@RestController
@RequestMapping("/api/userOrder")
class UserOrderController(
    @Autowired private val userOrderRepository: UserOrderRepository,
    @Autowired private val statusOrderRepository: StatusOrderRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val orderRepository: OrdersRepository,
    @Autowired private val userCardRepository: UserCardRepository,
    @Autowired private val orderCompositionRepository: OrderCompositionRepository,
    @Autowired private val addressesRepository: AddressRepository,
    @Autowired private val shopAddressesRepository: ShopAddressesRepository,
    @Autowired private val typeDeliveryRepository: TypeDeliveryRepository,
    @Autowired private val cartRepository: CartRepository,
    @Autowired private val deliveryInfoRepository: DeliveryInfoRepository,
    @Autowired private val clothesRepository: ClothesRepository,
    @Autowired private val colorRepository: ColorRepository,
    @Autowired private val sizeClothesRepository: SizeClothesRepository,
    ) {
    /**
     * Retrieves a list of all user orders.
     *
     * @return List<UserOrder> Returns a list containing all UserOrder entities.
     */
    @GetMapping("")
    fun getAllUserOrders(): List<UserOrder> =
        userOrderRepository.findAll().toList()

    /**
     * Handles the creation of a new user order based on the provided OrderDTO.
     *
     * @param userOrder Represents the OrderDTO containing user-specific order details.
     * @return ResponseEntity<Any> Returns a response indicating the status of the order creation.
     */
    @PostMapping("")
    fun createUserOrder(userOrder: OrderDTO): ResponseEntity<Any> {
        // Create new instances for UserOrder, Order, OrderComposition, and DeliveryInfo
        val newUserOrder = UserOrder()  // Represents the user-specific order details
        val order = Order()  // Represents the overall order information
        var orderComp = OrderComposition()  // Represents the composition of each item in the order
        val deliveryInfo = DeliveryInfo()  // Represents delivery-related information

        // Generate a unique order number based on the current date and a random number
        val generateNumber = Random().nextInt(99999)
        // Random number for uniqueness
        val generatedOrderNumber = generateNumber.toString() + Calendar.YEAR.toString() + Calendar.MONTH.toString() + Calendar.DAY_OF_YEAR.toString()

        // Fetch current order status (1 - means order created by default),
        // user card details (by id of user card from UserCard Entity),
        // and current user
        // from respective repositories
        val currentStatus = statusOrderRepository.findById(1).orElse(null)  // Default status for new orders
        val userCard = userCardRepository.findById(userOrder.userCardId).orElse(null)  // User card containing payment and user details
        val currentUser = userRepository.findById(userCard.user.id).orElse(null)  // Details of the user placing the order

        // Firstly, generates an instance of new order
        // Set order details using the generated order number and other relevant information
        order.numberOrder = generatedOrderNumber
        order.dateOrder = LocalDate.now().toString()  // Current date of the order
        order.timeOrder = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))  // Current time of the order formatted to HH:mm:ss, example "11:35:34"
        order.currentStatus = currentStatus  // Initial status for a new order (1 - means order created by default)
        order.sumOrder = userOrder.sumOrder  // Total sum of the order
        order.userCard = userCard  // Linking the order to the user's payment card

        // Save the order details to the repository for persistence
        orderRepository.save(order)

        // Save each item in cart to OrderComposition table
        // From user comes DTO object OrderDTO which contains a list of Long values representing id of each item in cart
        for (item in userOrder.orderComp) {
            val clothesQuantity = cartRepository.findById(item).orElse(null)  // Fetching details of each item in the cart of user
            orderComp.quantity = clothesQuantity.quantity  // Quantity of the specific clothing item in cart
            orderComp.clothesComp = clothesRepository.findById(clothesQuantity.colorClothesCart.clothes.idClothes).orElse(null)  // Clothing details
            orderComp.orderId = orderRepository.findById(order.idOrder).orElse(null)  // Linking the composition to the overall order
            orderComp.colorClothes = colorRepository.findById(clothesQuantity.colorClothesCart.colors.colorId).orElse(null)  // Color details
            orderComp.sizeClothes = sizeClothesRepository.findById(clothesQuantity.sizeClothes.sizeClothes.id).orElse(null)  // Size details
            orderCompositionRepository.save(orderComp)  // Save the composition to the repository
            orderComp = OrderComposition()  // Reset OrderComposition for the next iteration
        }

        // Set user and order details for UserOrder and save it to the repository
        newUserOrder.user = currentUser  // Linking the UserOrder to the current user
        newUserOrder.orders = orderRepository.findById(order.idOrder).orElse(null)  // Linking the UserOrder to the overall order
        userOrderRepository.save(newUserOrder)  // Save the UserOrder to the repository

        // Set delivery information based on the type of delivery chosen by the user
        deliveryInfo.order = orderRepository.findById(order.idOrder).orElse(null)  // Linking the delivery info to the overall order
        deliveryInfo.typeDelivery = typeDeliveryRepository.findById(userOrder.typeDelivery).orElse(null)  // Type of delivery chosen by the user where 1 is pick up, 2 - home (deliver home)

        if (userOrder.typeDelivery.toInt() == 1) {
            // If the delivery type is from the shop, set shop address and nullify user address
            deliveryInfo.shopAddresses = userOrder.shopAddress?.let { shopAddressesRepository.findById(it).orElse(null) }!!
            deliveryInfo.addresses = null
        } else {
            // If the delivery type is to the user, set user address and nullify shop address
            deliveryInfo.addresses = userOrder.userAddress?.let { addressesRepository.findById(it).orElse(null) }!!
            deliveryInfo.shopAddresses = null
        }

        // Save the delivery information to the repository
        deliveryInfoRepository.save(deliveryInfo)

        // Return a response indicating the successful formation of the order
        return ResponseEntity(Message("Order formed"), HttpStatus.CREATED)
    }



    /**
     * Retrieves all user orders associated with a specific user identified by the provided user ID.
     *
     * @param userId The unique identifier of the user for whom to retrieve orders.
     * @return ResponseEntity containing the list of UserOrder objects if found, or a NOT_FOUND status if no orders are found.
     */
    @GetMapping("/{id}")
    fun getAllUserOrderById(@PathVariable("id") userId: Long): ResponseEntity<List<UserOrder>> {
        // Retrieve all orders made by user with specific userId from the repository based on the provided user ID.
        val userOrder = userOrderRepository.findAllByUserId(userId)

        // Check if user orders were found, then return with OK status. Otherwise, return with NOT_FOUND status.
        return if (userOrder != null) ResponseEntity(userOrder, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    /**
     * Handles the deletion of a user order based on the provided userOrderId.
     *
     * @param userOrderId Represents the ID of the UserOrder to be deleted.
     * @return ResponseEntity<UserOrder> Returns a response indicating the status of the deletion.
     * I think it's mostly useless? 'cause you cannot actually delete order that was made by user
     * Might delete it later
     */
    @DeleteMapping("/{id}")
    fun deleteUserOrderById(@PathVariable("id") userOrderId: Long): ResponseEntity<UserOrder> {
        // Check if the UserOrder with the given ID exists
        if (!userOrderRepository.existsById(userOrderId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)  // If not found, return 404 NOT FOUND status
        }

        // Delete the UserOrder with the given ID
        userOrderRepository.deleteById(userOrderId)

        // Return 200 OK status indicating successful deletion
        return ResponseEntity(HttpStatus.OK)
    }

}