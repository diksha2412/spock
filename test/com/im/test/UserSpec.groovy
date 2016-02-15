package com.im.test

import spock.lang.IgnoreRest
import spock.lang.Specification
import spock.lang.Unroll

class UserSpec extends Specification {

    def "First test"() {
        expect:
        true
    }

    @Unroll("#sno")
    @IgnoreRest
    def "checking user's full name"() {
        given: "a user"
        User user = new User(firstName: firstName, lastName: lastName)

        when:
        String fullName = user.getFullName()

        then:
        fullName == output

        where:
        sno | firstName | lastName | output
        1   | null      | "ahuja"  | "null ahuja"
        2   | "diksha"  | null     | "diksha null"
        3   | "diksha"  | "ahuja"  | "diksha ahuja"
        4   | null      | null     | "null null"
        5   | ""        | ""       | " "
    }

    def "display Name"() {
        given: "a user"
        User user = new User(firstName: x, lastName: y, gender: z)

        when:
        String a = user.displayName()

        then:
        a == r

        where:
        x        | y       | z        | r
        "diksha" | "ahuja" | "Female" | "Msdiksha ahuja"
        "pulkit" | "ahuja" | "Male"   | "Mrpulkit ahuja"
    }

    def "is valid password"() {
        given: "a user"
        User user = new User(password: password)

        when:
        boolean res = user.isValidPassword(user.password)

        then: "check for valid password"
        res == pwdresult

        where:
        password   | pwdresult
        null       | false
        "test"     | false
        "test1234" | true

    }

    def "mocking encrypt password"() {
        given: "a user"
        User user = new User()

        and: "mocked passwordEncrypterSerive"
        def mockedPasswordEncrpterService = Mock(PasswordEncrypterService)
        user.passwordEncrypterService = mockedPasswordEncrpterService

        and: "creating a stub"
        mockedPasswordEncrpterService.encrypt(_) >> { result }

        when:
        String output = user.encyryptPassword(password)

        then:
        output == result

        where:
        password    | result
        "test"      | null
        "bchdndjkh" | "ok"
        null        | null

    }

    def "reset password and send email"() {
        given: "a user"
        User user = new User(password: "bhjcvd")

        and: "email service mock"
        def mockedEmailService = Mock(EmailService)
        user.emailService = mockedEmailService

        when:
        user.resetPasswordAndSendEmail()

        then:
        mockedEmailService.sendCancellationEmail(user, _ as String)
    }

    @Unroll
    def "Getting user income group when #incomePerMonth"() {
        setup: "A user having some monthly income"
        User user = new User(incomePerMonth: incomePerMonth);

        when:
        String group = user.getIncomeGroup();

        then:
        group == result

        where:
        incomePerMonth | result
        4000           | "MiddleClass"
        7000           | "Higher MiddleClass"
        14000          | "Higher Class"
    }

    def "Product purchased"() {
        given: "A user with no purchased products"
        User user = new User(purchasedProducts: [])

        and: "A product"
        Product product = new Product()

        when: "user.purchase() is called"
        user.purchase(product);

        then:
        user.purchasedProducts.contains(product) == true;
    }

    def "Cancel purchase"() {
        given: "A product"
        Product product = new Product();

        and: "A user with purchased product"
        User user = new User(purchasedProducts: [product]);

        when: "user.cancelPurchase() is called"
        user.cancelPurchase(product);

        then:
        user.purchasedProducts.contains(product) == false;
    }

}