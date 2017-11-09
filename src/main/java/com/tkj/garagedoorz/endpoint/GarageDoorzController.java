package com.tkj.garagedoorz.endpoint;

import com.tkj.garagedoorz.domain.DoorStatus;
import com.tkj.garagedoorz.domain.HwController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller that maps REST end points to Garage door hardware status and control.
 * @author Thomas G. Kenny Jr
 *
 */
@RestController
public class GarageDoorzController {

	private final HwController hwController;

	public GarageDoorzController( final HwController hwController ) {
		this.hwController = hwController;
	}

	/**
	 * @return Hello Doorz
	 */
	@RequestMapping ("/" )
	public String HelloDoorz() {
		return "Hello Doorz";
	}
	
	/**
	 * @return The status of the Garage.
	 */
	@RequestMapping( "/getGarageStatus" )
	public List<DoorStatus> getGarageStatus() {
		return hwController.getGarageDoorStatuses();
	}
	
	/**
	 * Return if the door is closed. At present we do not have any in motion state information.
	 * @param door Index of door to read position state. Defaults to 0 (first door).
	 * @return True if door is closed.
	 */
	@RequestMapping( "/{door}/isDoorClosed" )
	public boolean isDoorClosed( @PathVariable int door ) {
		return hwController.isDoorClosed( door );
	}
	
	/**
	 * Press the requested garage door button.
	 * @param door Index of door to actuate. Defaults to 0 (first door).
	 */
	@RequestMapping( "/{door}/pressDoorButton" )
	@ResponseStatus( HttpStatus.ACCEPTED )
	public void pressDoorButton( @PathVariable int door ) {
		hwController.pressDoorButton( door );
	}
	
	/**
	 * Opens the requested door if isClosed reports true. This function can erroneously stop the door if it
	 * is in motion when called. Door isClosed reports false as soon as the door is not closed and the
	 * state of motion is unknown at this time.
	 * @param door Index of door to actuate. Defaults to 0 (first door).
	 * @return True if presssDoorButton is called
	 */
	@RequestMapping( "/{door}/openDoor" )
	@ResponseStatus( HttpStatus.ACCEPTED )
	public boolean openDoor( @PathVariable int door ) {
		if(hwController.isDoorClosed( door )) {
			hwController.pressDoorButton( door );
			return true;
		}
		return false;
	}

	/**
	 * Closes the requested door if isClosed reports false. This function can erroneously stop the door if it
	 * is in motion when called. Door isClose reports false as soon as the door is not closed and the
	 * state of motion is unknown at this time.
	 * @param door Index of door to actuate. Defaults to 0 (first door).
	 * @return True if presssDoorButton is called
	 */
	@RequestMapping( "/{door}/closeDoor" )
	@ResponseStatus( HttpStatus.ACCEPTED )
	public boolean closeDoor( @PathVariable int door ) {
		if(!hwController.isDoorClosed( door )) {
			hwController.pressDoorButton( door );
			return true;
		}
		return false;
	}

}
