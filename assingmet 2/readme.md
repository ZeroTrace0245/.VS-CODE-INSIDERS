This project is a simple Node.js + Express app that:
- accepts a name using a `POST` form submission,
- redirects to a `GET` route,
- shows a greeting message.

## 1. Prerequisites

Make sure these are installed:
- Node.js (LTS recommended)
- npm (comes with Node.js)

## 2. Open the Project Folder

In terminal:

```powershell
cd "your location"
```

## 3. Install Dependencies

If not already installed:

```powershell
npm install
```

This installs Express from `package.json`.

## 4. Start the Server

Run:

```powershell
npm start
```

You should see:

```text
Server running at http://localhost:3003
```

## 5. Open in Browser

Go to:

```text
http://localhost:3003
```

## 6. Test the Main Flow

1. Enter a name in the form on the home page.
2. Submit the form.
3. The app sends a `POST` request to `/submit`.
4. Server validates the input.
5. Server redirects to `/welcome?name=...` using `GET`.
6. You should see a greeting like `Hello, YourName!`.

## 7. Test Validation (Empty Input)

1. Submit the form without entering a name.
2. The app redirects back to `/` with an error query parameter.
3. You should remain on the form page and be asked to provide a name.

## 8. Routes Overview

- `POST /submit`: receives form data (`name`) and redirects.
- `GET /welcome`: reads name from query string (or last submitted value) and renders greeting HTML.
- `GET /styles.css`: served from the `public` folder.

## 9. Stop the Server

In terminal, press:

```text
Ctrl + C
```

## 10. Troubleshooting

- Port already in use:
  - Set a different port before starting:
  ```powershell
  $env:PORT=3005; npm start
  ```
- `npm` not found:
  - Reinstall Node.js and reopen terminal.
- Page not loading:
  - Confirm server is running and open the exact URL: `http://localhost:3003`.
