/**
 * Order type definitions matching backend DTOs
 */

// Order status enum matching backend OrderStatus
export enum OrderStatus {
  PENDING = 'PENDING',
  PROCESSING = 'PROCESSING',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

// Checkout request with payment card details
export interface CheckoutRequest {
  cardNumber: string;
  cardHolderName: string;
  expiryDate: string;
  cvv: string;
}

// Order item in an order (product snapshot at time of purchase)
export interface OrderItemDTO {
  id: number;
  productId: number;
  productName: string;
  productImageUrl: string;
  quantity: number;
  priceAtPurchase: number;
  subtotal: number;
}

// Complete order response
export interface OrderResponse {
  id: number;
  orderNumber: string;
  status: OrderStatus;
  totalAmount: number;
  orderedAt: string;
  updatedAt: string;
  items: OrderItemDTO[];
}

// Admin request to update order status
export interface UpdateOrderStatusRequest {
  status: OrderStatus;
}
