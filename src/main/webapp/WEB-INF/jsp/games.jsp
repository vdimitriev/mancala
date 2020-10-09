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
</head>
<body>
<nav id="mainNav" class="navbar navbar-default navbar-fixed-top navbar-custom">
    <div class="container-fluid">
        <div class="navbar-header">
            <li><a class="navbar-brand" href="/games">Mancala</a></li>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-left">
               <c:if test="${pageContext.request.userPrincipal.name != null and pageContext.request.userPrincipal.name == 'admin'}">
                    <li><a href="/h2-console">Database admin</a></li>
               </c:if>
            </ul>
            <ul class="nav navbar-nav navbar-right">
               <li><a href="#" id="username" disabled>${pageContext.request.userPrincipal.name}</a></li>
               <li><a href="#" onclick="document.forms['logoutForm'].submit()">Logout</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <div class="row text-center">
        <table id="gamestable">
          <caption><a href="#" onclick="createnewgame('${pageContext.request.userPrincipal.name}')">Available games</a></caption>
          <tr>
            <th>Date of creation</th>
            <th>Game name</th>
            <th>Created by</th>
            <th>Playing with</th>
            <th></th>
          </tr>
          <c:forEach var="game" items="${availablegames}">
          <tr>
            <td>
                <fmt:formatDate type = "both" value = "${game.creationDate}" />
            </td>
            <td>${game.name}</td>
            <td>
                <c:choose>
                    <c:when test="${game.playerOne != null}">
                        ${game.playerOne.username}
                    </c:when>
                    <c:otherwise>
                        <a href="#" onclick="joingame('${game.id}','${pageContext.request.userPrincipal.name}', 1)">Join</a>
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${game.playerTwo != null}">
                        ${game.playerTwo.username}
                    </c:when>
                    <c:otherwise>
                        -
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="#" onclick="joingame('${game.id}','${pageContext.request.userPrincipal.name}', 2)">Join</a>
            </td>
          </tr>
          </c:forEach>
        </table>
    </div>
    <div class="row text-center">
        <table id="gamestable">
          <caption><a href="#" onclick="createnewgame('${pageContext.request.userPrincipal.name}')">My games</a></caption>
          <tr>
            <th>Date of creation</th>
            <th>Game name</th>
            <th>Created by</th>
            <th>Playing with</th>
            <th></th>
          </tr>
          <c:forEach var="game" items="${mygames}">
          <tr>
            <td>
                <fmt:formatDate type = "both" value = "${game.creationDate}" />
            </td>
            <td>${game.name}</td>
            <td>
                <c:choose>
                    <c:when test="${game.playerOne != null}">
                        ${game.playerOne.username}
                    </c:when>
                    <c:otherwise>
                        -
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${game.playerTwo != null}">
                        ${game.playerTwo.username}
                    </c:when>
                    <c:otherwise>
                        -
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="#" onclick="resumegame('${game.id}','${pageContext.request.userPrincipal.name}')">Resume</a>
            </td>
          </tr>
          </c:forEach>
        </table>
    </div>
    <br />
    <br />
    <div class="row text-center">
        <table id="gamestable">
          <tr>
              <th><a href="#" onclick="createnewgame('${pageContext.request.userPrincipal.name}')">Create new game -></a></th>
           </tr>
        </table>
     </div>
</div>
<c:if test="${pageContext.request.userPrincipal.name != null}">
    <form id="logoutForm" method="POST" action="${contextPath}/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</c:if>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/resources/js/stomp.js"></script>
<script type="text/javascript" src="/resources/js/sockjs.js"></script>
<script type="text/javascript" src="/resources/js/game.js"></script>
</body>
</html>