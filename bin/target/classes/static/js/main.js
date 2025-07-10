const app = document.getElementById('app');

const API_BASE_URL = '/api';
let token = localStorage.getItem('token');

async function apiFetch(url, options = {}) {
    const headers = { 'Content-Type': 'application/json' };
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE_URL}${url}`, {
        ...options,
        headers,
    });

    if (response.status === 401) {
        logout();
        renderLogin();
        return;
    }

    if (!response.ok) {
        throw new Error('API request failed');
    }

    return response.json();
}

function renderLogin() {
    app.innerHTML = `
        <h1>Login</h1>
        <form id="login-form">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit">Login</button>
        </form>
        <p>Don't have an account? <a href="#" id="show-signup">Sign up</a></p>
    `;

    document.getElementById('login-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData.entries());
        try {
            const response = await apiFetch('/auth/login', { method: 'POST', body: JSON.stringify(data) });
            token = response.accessToken;
            localStorage.setItem('token', token);
            renderProducts();
        } catch (error) {
            console.error('Login failed:', error);
        }
    });

    document.getElementById('show-signup').addEventListener('click', renderSignup);
}

function renderSignup() {
    app.innerHTML = `
        <h1>Sign Up</h1>
        <form id="signup-form">
            <input type="text" name="username" placeholder="Username" required>
            <input type="email" name="email" placeholder="Email" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit">Sign Up</button>
        </form>
        <p>Already have an account? <a href="#" id="show-login">Login</a></p>
    `;

    document.getElementById('signup-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData.entries());
        try {
            await apiFetch('/auth/signup', { method: 'POST', body: JSON.stringify(data) });
            renderLogin();
        } catch (error) {
            console.error('Signup failed:', error);
        }
    });

    document.getElementById('show-login').addEventListener('click', renderLogin);
}

async function renderProducts() {
    try {
        const products = await apiFetch('/products');
        app.innerHTML = `
            <h1>Products</h1>
            <div class="products"></div>
            <button id="view-cart">View Cart</button>
            <button id="view-orders">View Orders</button>
            <button id="logout">Logout</button>
        `;

        const productsContainer = app.querySelector('.products');
        products.content.forEach(product => {
            const productEl = document.createElement('div');
            productEl.className = 'product';
            productEl.innerHTML = `
                <img src="${product.imageUrl || 'https://via.placeholder.com/150'}" alt="${product.name}">
                <h2>${product.name}</h2>
                <p>$${product.price}</p>
                <button class="add-to-cart" data-id="${product.id}">Add to Cart</button>
            `;
            productsContainer.appendChild(productEl);
        });

        document.querySelectorAll('.add-to-cart').forEach(button => {
            button.addEventListener('click', async (e) => {
                const productId = e.target.dataset.id;
                await apiFetch(`/cart?productId=${productId}&quantity=1`, { method: 'POST' });
                alert('Product added to cart');
            });
        });

        document.getElementById('view-cart').addEventListener('click', renderCart);
        document.getElementById('view-orders').addEventListener('click', renderOrders);
        document.getElementById('logout').addEventListener('click', logout);
    } catch (error) {
        console.error('Failed to load products:', error);
    }
}

async function renderCart() {
    try {
        const cart = await apiFetch('/cart');
        app.innerHTML = `
            <h1>Cart</h1>
            <div class="cart-items"></div>
            <button id="place-order">Place Order</button>
            <button id="back-to-products">Back to Products</button>
        `;

        const cartItemsContainer = app.querySelector('.cart-items');
        if (cart.items.length === 0) {
            cartItemsContainer.innerHTML = '<p>Your cart is empty.</p>';
        } else {
            cart.items.forEach(item => {
                const itemEl = document.createElement('div');
                itemEl.innerHTML = `
                    <p>${item.productName} - Quantity: ${item.quantity}</p>
                    <button class="remove-from-cart" data-id="${item.id}">Remove</button>
                `;
                cartItemsContainer.appendChild(itemEl);
            });
        }

        document.querySelectorAll('.remove-from-cart').forEach(button => {
            button.addEventListener('click', async (e) => {
                const itemId = e.target.dataset.id;
                await apiFetch(`/cart/${itemId}`, { method: 'DELETE' });
                renderCart();
            });
        });

        document.getElementById('place-order').addEventListener('click', async () => {
            await apiFetch('/orders', { method: 'POST' });
            alert('Order placed successfully');
            renderProducts();
        });

        document.getElementById('back-to-products').addEventListener('click', renderProducts);
    } catch (error) {
        console.error('Failed to load cart:', error);
    }
}

async function renderOrders() {
    try {
        const orders = await apiFetch('/orders');
        app.innerHTML = `
            <h1>My Orders</h1>
            <div class="orders"></div>
            <button id="back-to-products">Back to Products</button>
        `;

        const ordersContainer = app.querySelector('.orders');
        if (orders.length === 0) {
            ordersContainer.innerHTML = '<p>You have no orders.</p>';
        } else {
            orders.forEach(order => {
                const orderEl = document.createElement('div');
                orderEl.innerHTML = `
                    <h3>Order #${order.id} - ${order.status}</h3>
                    <p>Date: ${new Date(order.orderDate).toLocaleDateString()}</p>
                    <p>Total: $${order.totalAmount}</p>
                `;
                ordersContainer.appendChild(orderEl);
            });
        }

        document.getElementById('back-to-products').addEventListener('click', renderProducts);
    } catch (error) {
        console.error('Failed to load orders:', error);
    }
}

function logout() {
    token = null;
    localStorage.removeItem('token');
    renderLogin();
}

if (token) {
    renderProducts();
} else {
    renderLogin();
}