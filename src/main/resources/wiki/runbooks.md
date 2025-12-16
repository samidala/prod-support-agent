# SRE Runbooks

## Service Restart Procedure
To restart a service in the production cluster:
1.  Login to the bastion host.
2.  Run `kubectl rollout restart deployment <service-name> -n production`.
3.  Monitor the rollout: `kubectl rollout status deployment <service-name>`.

## Database Maintenance
- **Vacuuming**: Run weekly on Sunday at 2 AM.
- **Connection Killing**: Use `SELECT pg_terminate_backend(pid) ...` to kill stuck queries.

## Emergency Contacts
- **Backend Lead**: Alice (ext 101)
- **Infra Lead**: Bob (ext 102)
