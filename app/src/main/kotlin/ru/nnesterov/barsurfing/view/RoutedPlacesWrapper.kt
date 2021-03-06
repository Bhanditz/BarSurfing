/*
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <nikolaynesterov@gmail.com> wrote this file.  As long as you retain this notice you
 *  can do whatever you want with this stuff. If we meet some day, and you think
 *  this stuff is worth it, you can buy me a beer in return.   Nikolay Nesterov
 */

package ru.nnesterov.barsurfing.view

import com.google.android.gms.maps.model.LatLng
import ru.nnesterov.barsurfing.domain.places.Place
import ru.nnesterov.barsurfing.domain.places.Route
import ru.nnesterov.barsurfing.domain.places.RoutedPlaces
import java.io.Serializable
import java.util.*

/**
 *  Hacky class to encapsulate serialization problems
 */
internal class RoutedPlacesWrapper(routedPlaces: RoutedPlaces) : Serializable {

    private val serializationUUID: Long = 248L

    private val internalPlaces = ArrayList<InternalPlace>()

    private val internalRoutePoints = ArrayList<InternalPoint>()

    private var distance = 0

    init {
        routedPlaces.route.points.forEach {
            internalRoutePoints.add(InternalPoint(it.latitude, it.longitude))
        }

        routedPlaces.places.forEach {
            internalPlaces.add(InternalPlace(it.id,
                    it.name,
                    it.address,
                    InternalPoint(it.latitude, it.longitude),
                    it.visited))
        }

        distance = routedPlaces.route.distanceMeters
    }

    internal fun toRoutedPlaces(): RoutedPlaces {
        val places = ArrayList<Place>(internalPlaces.size)
        internalPlaces.forEach {
            places.add(Place(it.id,
                    it.name,
                    it.address,
                    it.location.latitude,
                    it.location.longitude,
                    it.visited
            ))
        }

        val routePoints = ArrayList<LatLng>(internalRoutePoints.size)
        internalRoutePoints.forEach {
            routePoints.add(LatLng(it.latitude,
                    it.longitude))
        }

        return RoutedPlaces(places, Route(routePoints, distance))
    }

    private data class InternalPoint(val latitude: Double,
                                     val longitude: Double) : Serializable {
        private val serializationUUID: Long = 249L
    }

    private data class InternalPlace(val id: String,
                                     val name: String,
                                     val address: String,
                                     val location: InternalPoint,
                                     val visited: Boolean) : Serializable {
        private val serializationUUID: Long = 250L
    }

}



