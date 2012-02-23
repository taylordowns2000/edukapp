<form type="POST" id="login_form" action="login" class="form-inline" style="display: none; margin-bottom: 0; margin-left: 15px">
  <input type="hidden" name="from" value="${pageContext.request.requestURI}?${pageContext.request.queryString}">
  <input type="hidden" name="from-q" value="${pageContext.request.queryString}">
  <input autocomplete="off" id="name" name="username" type="text" class="input-small" placeholder="Email">
  <input autocomplete="off" id="pass"	name="password" type="password" class="input-small" placeholder="Password">
  <button type="submit" class="btn" style="margin: 5px">log in</button>
</form> 