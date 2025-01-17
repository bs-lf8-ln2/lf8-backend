import requests
import sys
import argparse

# 1. pip install requests
# 2. run python .\quickupdate.py --update-file
# 3. ???
# 4. Profit

def get_access_token():
    url = "https://keycloak.szut.dev/auth/realms/szut/protocol/openid-connect/token"
    payload = {
        "grant_type": "password",
        "client_id": "employee-management-service",
        "username": "user",
        "password": "test"
    }
    headers = {
        "Content-Type": "application/x-www-form-urlencoded"
    }

    # Make the POST request
    response = requests.post(url, data=payload, headers=headers)

    # Check if the request was successful
    if response.status_code == 200:
        # Parse the JSON response
        response_data = response.json()

        # Extract the access token
        access_token = response_data.get("access_token")

        # Return the access token
        if access_token:
            return access_token
        else:
            print("Error: Access Token not found in the response.", file=sys.stderr)
            return None
    else:
        print(f"Error: Failed to fetch token (status code: {response.status_code}).", file=sys.stderr)
        return None

def update_http_file(access_token):
    # Define the .http file path
    http_file_path = "SampleRequests.http"
    try:
        with open(http_file_path, 'r') as file:
            lines = file.readlines()

        # Update the access token in the first line
        lines[0] = f"@token = {access_token}\n"

        # Write the updated content back to the file
        with open(http_file_path, 'w') as file:
            file.writelines(lines)

        print(f"Updated {http_file_path} with new access token.")
    except FileNotFoundError:
        print(f"Error: {http_file_path} not found.", file=sys.stderr)

def main():
    parser = argparse.ArgumentParser(description='Fetch access token and optionally update .http file.')
    parser.add_argument('--update-file', action='store_true', help='Update SampleRequests.http with new access token')
    args = parser.parse_args()

    access_token = get_access_token()

    if access_token:
        print(access_token)
        if args.update_file:
            update_http_file(access_token)

if __name__ == "__main__":
    main()