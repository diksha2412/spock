package com.im.test

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class TransactionSpec extends Specification {

    @Unroll("#sno")
   def "testing sell when no exception is thrown"(){
       given:"a product"
       Product product=new Product(price: price)

       and:"a user"
       User user=new User(balance: balance)

       and:"a transaction"
       Transaction transaction=new Transaction()

       when:"sell() is called"
       transaction.sell(product, user)

       then:
       output==balance-price

       where:
       sno | balance | price | output
       1   | 200     | 100   | 100
       2   | 100     | 100   | 0
   }

    def "testing sell() when exceptiion is thrown"(){
        given:"a product"
        Product product=new Product(price: 100)

        and:"a user"
        User user=new User(balance: 50)

        and:"a transaction"
        Transaction transaction=new Transaction()

        when:"sell() is called"
        transaction.sell(product, user)

        then:"exception is thrown"
        def e=thrown(SaleException)
        e.message=="Not enough account balance"
    }

    def "cancellation of sale"() {

        given:
        Product product = new Product(name: name, price: price)

        and:
        User user = new User(balance: bal)

        and:
        Transaction transaction = new Transaction()

        and:
        def mockedEmailService = Mock(EmailService)
        transaction.emailService = mockedEmailService

        when:
        transaction.cancelSale(product, user)

        then:
        mockedEmailService.sendCancellationEmail(user, _ as String)

        where:
        name  | price | bal  || output
        "xyz" | 500   | 1000 || true
    }


    @Unroll("#sno")
    def "testing calculate discount"(){
        given:"a product"
        Product product=new Product(discountType: discountType, price: 300)

        and:"a user"
        User user=new User(isPrivellegedCustomer: isPrivellegedCustomer)

        and:"a transaction"
        Transaction transaction=new Transaction()

        when:"method is called"
        BigDecimal discount=transaction.calculateDiscount(product, user)

        then:
        output==discount

        where:
        sno | discountType                   | isPrivellegedCustomer | output
        1   | DiscountType.NONE            | true                  | 0
        2   | DiscountType.NONE            | false                 | 0
        3   | DiscountType.PRIVELLEGE_ONLY | true                  | 90
        4   | DiscountType.PRIVELLEGE_ONLY | false                 | 30
    }


}
