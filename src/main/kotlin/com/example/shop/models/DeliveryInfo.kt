package com.example.shop.models
import com.example.shop.models.Address
import jakarta.persistence.*

@Entity
@Table(name = "delivery_info")
data class DeliveryInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0,

    //DONE
    @ManyToOne(cascade = [CascadeType.ALL])
    var order: Order = Order(), // order id

    //DONE
    @ManyToOne(cascade = [CascadeType.ALL])
    var typeDelivery: TypeDelivery = TypeDelivery(),

    //DONE
    @ManyToOne(cascade = [CascadeType.ALL])
    var shopAddresses: ShopAddresses? = ShopAddresses(), // if type delivery is pickup then this value is not null

    //DONE
    @ManyToOne(cascade = [CascadeType.ALL])
    var addresses: Address? = Address(), // if type delivery is home delivery then this value is not null
)
