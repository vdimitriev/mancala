<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "spring" uri="http://www.springframework.org/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>Mancala board game</title>
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="/resources/css/jquery-ui.min.css">
    <link rel="stylesheet" href="/resources/css/common.css">
    <link rel="stylesheet" href="/resources/css/main.css">
</head>
<body>
<nav id="mainNav" class="navbar navbar-default navbar-fixed-top navbar-custom">
    <div class="container-fluid">
        <div class="navbar-header">
            <li><a class="navbar-brand" href="/games">Mancala</a></li>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
               <li><a href="#" id="username" disabled>${pageContext.request.userPrincipal.name}</a></li>
               <li><a href="#" onclick="document.forms['logoutForm'].submit()">Logout</a></li>
            </ul>
        </div>
    </div>
</nav>
<form id="form" name="form">
    <div class="container">
        <div class="row text-center">
            <table id="gametable">
                <c:choose>
                    <c:when test="${game.isFinished()}">
                        <caption>The game has finished. Winner is ${game.winner}</caption>
                    </c:when>
                    <c:when test="${game.playerOne == null || game.playerTwo == null}">
                        <caption>Waiting for other player to join the game.</caption>
                    </c:when>
                    <c:when test="${game.playerTurnName == pageContext.request.userPrincipal.name}">
                        <caption>It is your turn, please click on a non empty pit.</caption>
                    </c:when>
                    <c:otherwise>
                        <caption>Waiting for the other player to make a move ...</caption>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td rowspan="6"><a class="btn btn-oval"><b>${game.pockets.get(13).elementCount}</b></a></td>
                    <c:choose>
                        <c:when test="${!game.isFinished() && game.playerOne != null && game.playerTwo != null && game.playerTurn == 2 && game.playerTurnName == pageContext.request.userPrincipal.name}">
                            <td><a onclick="moveone(13, 2, '${game.id}', '${game.pockets.get(12).elementCount}', '${pageContext.request.userPrincipal.name}');" class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(12).elementCount}</b></a></td>
                            <td><a onclick="moveone(12, 2, '${game.id}', '${game.pockets.get(11).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(11).elementCount}</b></a></td>
                            <td><a onclick="moveone(11, 2, '${game.id}', '${game.pockets.get(10).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(10).elementCount}</b></a></td>
                            <td><a onclick="moveone(10, 2, '${game.id}', '${game.pockets.get(9).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(9).elementCount}</b></a></td>
                            <td><a onclick="moveone(9, 2, '${game.id}', '${game.pockets.get(8).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(8).elementCount}</b></a></td>
                            <td><a onclick="moveone(8, 2, '${game.id}', '${game.pockets.get(7).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(7).elementCount}</b></a></td>
                        </c:when>
                        <c:otherwise>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(12).elementCount}</b></span></td>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(11).elementCount}</b></span></td>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(10).elementCount}</b></span></td>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(9).elementCount}</b></span></td>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(8).elementCount}</b></span></td>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(7).elementCount}</b></span></td>
                        </c:otherwise>
                    </c:choose>
                    <td rowspan="6"><span class="btn btn-oval"><b>${game.pockets.get(6).elementCount}</b></span></td>
                </tr>
                <tr>
                    <c:choose>
                        <c:when test="${!game.isFinished() && game.playerOne != null && game.playerTwo != null && game.playerTurn == 1 && game.playerTurnName == pageContext.request.userPrincipal.name}">
                            <td><a onclick="moveone(1, 1, '${game.id}', '${game.pockets.get(0).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(0).elementCount}</b></a></td>
                            <td><a onclick="moveone(2, 1, '${game.id}', '${game.pockets.get(1).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(1).elementCount}</b></a></td>
                            <td><a onclick="moveone(3, 1, '${game.id}', '${game.pockets.get(2).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(2).elementCount}</b></a></td>
                            <td><a onclick="moveone(4, 1, '${game.id}', '${game.pockets.get(3).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(3).elementCount}</b></a></td>
                            <td><a onclick="moveone(5, 1, '${game.id}', '${game.pockets.get(4).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(4).elementCount}</b></a></td>
                            <td><a onclick="moveone(6, 1, '${game.id}', '${game.pockets.get(5).elementCount}', '${pageContext.request.userPrincipal.name}');"  class="btn btn-primary btn-circle btn-xl"><b>${game.pockets.get(5).elementCount}</b></a></td>
                        </c:when>
                        <c:otherwise>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(0).elementCount}</b></span></td>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(1).elementCount}</b></span></td>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(2).elementCount}</b></span></td>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(3).elementCount}</b></span></td>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(4).elementCount}</b></span></td>
                            <td><span class="btn btn-circle btn-xl"><b>${game.pockets.get(5).elementCount}</b></span></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </table>
        </div>
    </div>
</form>
<div id="chat-container">
    <ul id="messageArea"></ul>
    <form id="messageForm" name="messageForm">
        <div class="input-message">
            <span>${pageContext.request.userPrincipal.name}</span>
            <input type="text" id="message" autocomplete="off" placeholder="Type a message..." />
            <button type="submit">Send</button>
        </div>
    </form>
</div>
<c:if test="${pageContext.request.userPrincipal.name != null}">
    <form id="logoutForm" method="POST" action="${contextPath}/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</c:if>
<span type="hidden" id="game">${game.id}</span>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/resources/js/stomp.js"></script>
<script type="text/javascript" src="/resources/js/sockjs.js"></script>
<script type="text/javascript" src="/resources/js/websocket.js"></script>
<script type="text/javascript" src="/resources/js/game.js"></script>
<script type="text/javascript" src="/resources/js/play.js"></script>
</body>
</html>