# Chatore API Documentation (OpenAPI / Swagger Design)

```yaml
openapi: 3.0.3
info:
  title: Chatore API
  description: |
    Chatore is a scalable food delivery platform inspired by modern food ordering systems.

    Features:
      - Authentication & Authorization
      - Restaurant Discovery
      - Cart Management
      - Order Management
      - Payments
      - Real-Time Tracking
      - Notifications

  version: 1.0.0

servers:
  - url: http://localhost:8080
    description: Local Development

security:
  - bearerAuth: []

components:

  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT

  schemas:

    ApiResponse:
      type: object
      properties:
        success:
          type: boolean
        message:
          type: string
        data:
          type: object

    ErrorResponse:
      type: object
      properties:
        success:
          type: boolean
        error:
          type: object
          properties:
            code:
              type: string
            message:
              type: string

    SignupRequest:
      type: object
      required:
        - name
        - email
        - phone
        - password
      properties:
        name:
          type: string
        email:
          type: string
        phone:
          type: string
        password:
          type: string

    LoginRequest:
      type: object
      required:
        - email
        - password
      properties:
        email:
          type: string
        password:
          type: string

    AuthResponse:
      type: object
      properties:
        accessToken:
          type: string
        refreshToken:
          type: string
        user:
          $ref: '#/components/schemas/User'

    User:
      type: object
      properties:
        id:
          type: integer
        name:
          type: string
        email:
          type: string
        phone:
          type: string
        role:
          type: string

    Address:
      type: object
      properties:
        id:
          type: integer
        label:
          type: string
        houseNo:
          type: string
        street:
          type: string
        city:
          type: string
        state:
          type: string
        pincode:
          type: string
        latitude:
          type: number
          format: double
        longitude:
          type: number
          format: double
        isDefault:
          type: boolean

    Restaurant:
      type: object
      properties:
        id:
          type: integer
        name:
          type: string
        cuisineType:
          type: string
        rating:
          type: number
        deliveryTimeMinutes:
          type: integer
        imageUrl:
          type: string

    MenuItem:
      type: object
      properties:
        id:
          type: integer
        name:
          type: string
        description:
          type: string
        price:
          type: number
        isVeg:
          type: boolean
        imageUrl:
          type: string

    CartItemRequest:
      type: object
      required:
        - menuItemId
        - quantity
      properties:
        menuItemId:
          type: integer
        quantity:
          type: integer

    PlaceOrderRequest:
      type: object
      required:
        - addressId
        - paymentMethod
      properties:
        addressId:
          type: integer
        paymentMethod:
          type: string
          example: UPI
        specialInstructions:
          type: string

    Order:
      type: object
      properties:
        id:
          type: integer
        orderStatus:
          type: string
        paymentStatus:
          type: string
        totalAmount:
          type: number
        placedAt:
          type: string
          format: date-time

paths:

  /api/v1/auth/signup:
    post:
      tags:
        - Authentication
      summary: Register new user
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/SignupRequest'
      responses:
        '201':
          description: User created successfully

  /api/v1/auth/login:
    post:
      tags:
        - Authentication
      summary: Login user
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/LoginRequest'
      responses:
        '200':
          description: Login successful
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AuthResponse'

  /api/v1/auth/refresh:
    post:
      tags:
        - Authentication
      summary: Refresh access token
      responses:
        '200':
          description: Token refreshed

  /api/v1/auth/logout:
    post:
      tags:
        - Authentication
      summary: Logout user
      responses:
        '200':
          description: Logout successful

  /api/v1/users/me:
    get:
      tags:
        - Users
      summary: Get current user profile
      responses:
        '200':
          description: User profile fetched

    put:
      tags:
        - Users
      summary: Update current user profile
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/User'
      responses:
        '200':
          description: User updated

  /api/v1/users/addresses:
    get:
      tags:
        - Addresses
      summary: Get all user addresses
      responses:
        '200':
          description: Addresses fetched

    post:
      tags:
        - Addresses
      summary: Add new address
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Address'
      responses:
        '201':
          description: Address created

  /api/v1/users/addresses/{addressId}:
    put:
      tags:
        - Addresses
      summary: Update address
      parameters:
        - name: addressId
          in: path
          required: true
          schema:
            type: integer
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Address'
      responses:
        '200':
          description: Address updated

    delete:
      tags:
        - Addresses
      summary: Delete address
      parameters:
        - name: addressId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '204':
          description: Address deleted

  /api/v1/restaurants/nearby:
    get:
      tags:
        - Restaurants
      summary: Get nearby restaurants
      parameters:
        - name: lat
          in: query
          schema:
            type: number
        - name: lng
          in: query
          schema:
            type: number
        - name: page
          in: query
          schema:
            type: integer
        - name: size
          in: query
          schema:
            type: integer
      responses:
        '200':
          description: Nearby restaurants fetched

  /api/v1/restaurants/search:
    get:
      tags:
        - Restaurants
      summary: Search restaurants
      parameters:
        - name: q
          in: query
          schema:
            type: string
      responses:
        '200':
          description: Search results fetched

  /api/v1/restaurants/{restaurantId}:
    get:
      tags:
        - Restaurants
      summary: Get restaurant details
      parameters:
        - name: restaurantId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '200':
          description: Restaurant details fetched

  /api/v1/restaurants/{restaurantId}/menu:
    get:
      tags:
        - Restaurants
      summary: Get restaurant menu
      parameters:
        - name: restaurantId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '200':
          description: Menu fetched

  /api/v1/cart:
    get:
      tags:
        - Cart
      summary: Get user cart
      responses:
        '200':
          description: Cart fetched

    delete:
      tags:
        - Cart
      summary: Clear cart
      responses:
        '204':
          description: Cart cleared

  /api/v1/cart/items:
    post:
      tags:
        - Cart
      summary: Add item to cart
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CartItemRequest'
      responses:
        '201':
          description: Item added to cart

  /api/v1/cart/items/{itemId}:
    put:
      tags:
        - Cart
      summary: Update cart item quantity
      parameters:
        - name: itemId
          in: path
          required: true
          schema:
            type: integer
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              properties:
                quantity:
                  type: integer
      responses:
        '200':
          description: Cart updated

    delete:
      tags:
        - Cart
      summary: Remove item from cart
      parameters:
        - name: itemId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '204':
          description: Item removed

  /api/v1/orders:
    post:
      tags:
        - Orders
      summary: Place new order
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/PlaceOrderRequest'
      responses:
        '201':
          description: Order placed successfully

    get:
      tags:
        - Orders
      summary: Get user order history
      parameters:
        - name: page
          in: query
          schema:
            type: integer
        - name: size
          in: query
          schema:
            type: integer
      responses:
        '200':
          description: Orders fetched

  /api/v1/orders/{orderId}:
    get:
      tags:
        - Orders
      summary: Get order details
      parameters:
        - name: orderId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '200':
          description: Order details fetched

  /api/v1/orders/{orderId}/cancel:
    patch:
      tags:
        - Orders
      summary: Cancel order
      parameters:
        - name: orderId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '200':
          description: Order cancelled

  /api/v1/orders/{orderId}/tracking:
    get:
      tags:
        - Orders
      summary: Track order in real-time
      parameters:
        - name: orderId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '200':
          description: Tracking data fetched

  /api/v1/payments/initiate:
    post:
      tags:
        - Payments
      summary: Initiate payment
      responses:
        '200':
          description: Payment initiated

  /api/v1/payments/verify:
    post:
      tags:
        - Payments
      summary: Verify payment
      responses:
        '200':
          description: Payment verified

  /api/v1/payments/history:
    get:
      tags:
        - Payments
      summary: Get payment history
      responses:
        '200':
          description: Payment history fetched

  /api/v1/notifications:
    get:
      tags:
        - Notifications
      summary: Get user notifications
      responses:
        '200':
          description: Notifications fetched

  /api/v1/notifications/{notificationId}/read:
    patch:
      tags:
        - Notifications
      summary: Mark notification as read
      parameters:
        - name: notificationId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '200':
          description: Notification marked as read

  /api/v1/restaurants/{restaurantId}/reviews:
    get:
      tags:
        - Reviews
      summary: Get restaurant reviews
      parameters:
        - name: restaurantId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '200':
          description: Reviews fetched

    post:
      tags:
        - Reviews
      summary: Add restaurant review
      parameters:
        - name: restaurantId
          in: path
          required: true
          schema:
            type: integer
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              properties:
                rating:
                  type: integer
                comment:
                  type: string
      responses:
        '201':
          description: Review added

  /api/v1/users/favorites:
    get:
      tags:
        - Favorites
      summary: Get favorite restaurants
      responses:
        '200':
          description: Favorite restaurants fetched

  /api/v1/users/favorites/{restaurantId}:
    post:
      tags:
        - Favorites
      summary: Add restaurant to favorites
      parameters:
        - name: restaurantId
          in: path
          required: true
          schema:
            type: integer
      responses:
        '201':
          description: Restaurant added to favorites
```

# Future Enhancements

## Delivery APIs
- Accept delivery
- Reject delivery
- Live location updates
- Delivery earnings

## Admin APIs
- Restaurant management
- Analytics dashboards
- User moderation
- Order monitoring

## Internal Service APIs
- Internal order events
- Kafka event publishing
- Health check endpoints
- Metrics endpoints

# Recommended Swagger Libraries

## Spring Boot

Use:

- springdoc-openapi-starter-webmvc-ui

Maven:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.5</version>
</dependency>
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI Docs:

```text
http://localhost:8080/v3/api-docs
```

