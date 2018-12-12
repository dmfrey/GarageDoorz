package com.tkj.garagedoorz.health

import com.tkj.garagedoorz.domain.GarageDoorzRepository
import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health.Builder
import org.springframework.stereotype.Component

/**
 * HealthIndicator that reports garage door statuses.
 * @author Thomas G. Kenny Jr
 */
@Component
class DoorStatusHealthIndicator( private val repository: GarageDoorzRepository ) : AbstractHealthIndicator() {

    @Throws( Exception::class )
    override fun doHealthCheck( builder: Builder ) {

        val doorStatuses = repository.garageDoorStatuses
        builder
                .up()
                .withDetail( "doors", doorStatuses )

    }

}
