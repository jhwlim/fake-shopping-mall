package com.example.domain.user

import com.example.common.enums.UserRole
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDateTime

@DataJpaTest
internal class UserRepositoryTest @Autowired constructor(
    val userRepository: UserRepository,
) : BehaviorSpec({

    Given("findByName - 일치하는 이름의 사용자가 DB에 존재하는 경우") {
        val name = "test"

        When("사용자 정보를 조회하면") {
            val actual = userRepository.findByName(name)

            Then("해당 사용자 정보를 반환해야 한다.") {
                actual shouldNotBe null
                actual?.let {
                    actual.id shouldBe 1L
                    actual.name shouldBe "test"
                    actual.password shouldBe "1234"
                    actual.role shouldBe UserRole.USER
                    actual.createdDateTime shouldBe LocalDateTime.of(2022, 8, 1, 0, 0, 0)
                    actual.updatedDateTime shouldBe LocalDateTime.of(2022, 8, 1, 0, 0, 0)
                }
            }

        }

    }

    Given("findByName - 일치하는 이름의 사용자가 DB에 존재하지 않는 경우") {
        val name = "name"

        When("사용자 정보를 조회하면") {
            val actual = userRepository.findByName(name)

            Then("null을 반환해야 한다.") {
                actual shouldBe null
            }

        }
    }

})
