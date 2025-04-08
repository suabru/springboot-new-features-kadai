const stripe = Stripe('pk_test_51R1JQdJe5ZXK2ZHLhbpqKA77d7H2gPux1dQnAUQGO2sNxRd88xlz9fZfE5etcFJVtbbnecLvyfJzzKd46qB06HbU00gfL0tQ3E');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
 stripe.redirectToCheckout({
   sessionId: sessionId
 })
});