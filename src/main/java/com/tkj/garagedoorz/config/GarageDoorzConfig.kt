package com.tkj.garagedoorz.config

import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioProvider
import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.test.MockGpioFactory
import com.pi4j.io.gpio.test.MockPin
import com.tkj.garagedoorz.domain.GarageDoor
import com.tkj.garagedoorz.domain.MockGarageDoor
import com.tkj.garagedoorz.domain.RpiGarageDoor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order

@Configuration
class GarageDoorzConfig {

    @Configuration
    @Profile( "default" )
    class GarageDoorzMockConfig {

        @Bean
        fun gpioProvider(): GpioProvider {

            val provider = MockGpioFactory.getMockProvider()

            provider.setMockState( MockPin.DIGITAL_INPUT_PIN, PinState.LOW )
            provider.setMockState( MockPin.DIGITAL_OUTPUT_PIN, PinState.LOW )

            return provider
        }

        @Bean
        fun gpioController(): GpioController {

            return MockGpioFactory.getInstance()
        }

        @Bean( "door1" )
        fun door1(
                gpio: GpioController
        ): GarageDoor {

            return MockGarageDoor( gpio )
        }

    }

    @Configuration
    @Profile( "pi" )
    class GarageDoorzPiConfig {

        @Bean
        fun gpioController(): GpioController {

            return GpioFactory.getInstance()
        }

        @Bean( "door1" )
        @Order( 1 )
        fun door1(
                gpio: GpioController,
                @Value( "\${garagedoorz.doors.jill.label}" ) doorName: String,
                @Value( "\${garagedoorz.doors.jill.actuator}" ) actuator: Int,
                @Value( "\${garagedoorz.doors.jill.position-sensor}" ) positionSensor: Int
        ): GarageDoor {

            return RpiGarageDoor( gpio, doorName, actuator, positionSensor )
        }

        @Bean( "door2" )
        @Order( 2 )
        fun door2(
                gpio: GpioController,
                @Value( "\${garagedoorz.doors.tom.label}" ) doorName: String,
                @Value( "\${garagedoorz.doors.tom.actuator}" ) actuator: Int,
                @Value( "\${garagedoorz.doors.tom.position-sensor}" ) positionSensor: Int
        ): GarageDoor {

            return RpiGarageDoor( gpio, doorName, actuator, positionSensor )
        }

    }

}
