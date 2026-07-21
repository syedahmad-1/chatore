
package com.chatore.restaurant.security;

import java.util.UUID;

public interface CurrentUserProvider {

    UUID getCurrentUserId();

}