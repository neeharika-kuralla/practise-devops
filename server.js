const http = require("http");

const version = process.env.APP_VERSION || "v1";

const server = http.createServer((req, res) => {
    res.writeHead(200, { "Content-Type": "text/plain" });
    res.end(`Node.js Application - Version: ${version}\n`);
});

server.listen(3000, "0.0.0.0", () => {
    console.log(`Application running on port 3000, version ${version}`);
});