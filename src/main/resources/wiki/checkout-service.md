# Checkout Service Troubleshooting Guide

## Overview
The Checkout Service handles payment processing and order finalization. It communicates with the Payment Gateway and the Inventory Service.

## Common Issues

### 500 Internal Server Errors
If you are seeing a spike in 500 errors, check the following:

1.  **Database Connection Pool**: The service is sensitive to DB connection exhaustion.
    - **Symptoms**: `JdbcSQLNonTransientConnectionException`, "Connection is not available".
    - **Root Cause**: Often caused by high traffic or a connection leak in the application code.
    - **Fix**: Check active connections. If maxed out, restart the pods. Long term: fix the leak.

2.  **Payment Gateway Timeout**:
    - **Symptoms**: `SocketTimeoutException` when calling `api.payment-provider.com`.
    - **Fix**: Check the status page of the payment provider.

## Recent Incidents
- **Incident #4920**: v2.0 deployment caused memory leak. Rolled back.
- **Incident #4921**: v2.1 deployment introduced a DB connection leak in the `PaymentProcessor` class.
