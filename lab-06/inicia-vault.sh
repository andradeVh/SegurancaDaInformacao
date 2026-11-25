#!/bin/bash
set -e
APP_NAME="seg"
ROLE_NAME="seg-role"
POLICY_NAME="seg-politica"
VAULT_CONTAINER_NAME="vault-server"
VAULT_ROOT_TOKEN="token-admin-dev-apenas"
VAULT_ADDR="http://127.0.0.1:8200"
read -r -p "Entre com o client id do GitHub: " GITHUB_ID
read -r -s -p "Entre com o client secret do GitHub: " GITHUB_SECRET
docker exec -i "$VAULT_CONTAINER_NAME" sh <<EOF
export VAULT_ADDR='$VAULT_ADDR'
vault login -no-print $VAULT_ROOT_TOKEN
vault kv put secret/$APP_NAME spring.security.oauth2.client.registration.github.client-id="$GITHUB_ID" \
spring.security.oauth2.client.registration.github.client-secret="$GITHUB_SECRET"
vault auth enable approle >/dev/null 2>&1
vault policy write $POLICY_NAME - <<POL
path "secret/data/$APP_NAME" {
capabilities = ["read"]
}
POL
vault write auth/approle/role/$ROLE_NAME token_ttl=1h token_max_ttl=1h policies="$POLICY_NAME"
vault write auth/approle/role/$ROLE_NAME/role-id role_id="seg-role-id"
EOF
SECRET_ID=$(docker exec -i "$VAULT_CONTAINER_NAME" sh -c "export VAULT_ADDR='$VAULT_ADDR' && vault login -no-print $VAULT_ROOT_TOKEN >/dev/null && vault write -field=secret_id -f auth/approle/role/$ROLE_NAME/secret-id")
if [ -z "$SECRET_ID" ]; then
    echo "Erro ao gerar Secret ID"
    exit 1
fi
echo "VAULT_SECRET_ID=$SECRET_ID" > .env