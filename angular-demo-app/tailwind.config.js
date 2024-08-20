/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts, js, jsx, tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'basic': "#0f172a",
        'basic-btn': "#1b294b",
        'basic-dark': "#070a13",
        'basic-component': "#365396",
      },
    },
  },
  plugins: [],
}

