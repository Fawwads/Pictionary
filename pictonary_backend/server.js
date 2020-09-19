var express = require("express");
var app = express();
var path = require("path");
var server = require("http").createServer(app);
var io = require("socket.io")(server);
var port = process.env.PORT || 3000;

server.listen(port, () => {
  console.log("Server listening at port %d", port);
});

// Routing
app.use(express.static(path.join(__dirname, "public")));

io.on("connection", (socket) => {

  socket.on("create_session", (data) => {
      console.log("create_session",data)
    socket.join(data.id);
  });

  socket.on("onStrokeStarted", (data) => {
    console.log("onStrokeStarted",data)
    socket.to(data.id).emit("onStrokeStarted", data);
  });

  socket.on("onStroke", (data) => {
    console.log("onStroke",data)
    socket.to(data.id).emit("onStroke", data);
  });

  socket.on("clear", (data) => {
    console.log("clear",data)
    socket.to(data.id).emit("clear", data);
  });

  // when the user disconnects.. perform this
  socket.on("disconnect", (data) => {
      socket.to(data.id).emit("user left");    
  });


});
