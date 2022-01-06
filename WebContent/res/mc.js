// https://www.w3resource.com/javascript/form/email-validation.php
// https://www.w3resource.com/javascript/form/password-validation.php
function validateLogIn() {
  var email = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
  var password = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
  var ok = true;
  var p = document.getElementById("email").value;
  if (!p.match(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/)) {
    alert("Invalid username format!");
    ok = false;
  }
  if (!ok) return false;

  p = document.getElementById("password").value;
  if (!p.match(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/)) {
    alert(
      "Password must be between 6 to 20 characters and contains at least one number, one uppercase letter and one lowercase letter!"
    );
    ok = false;
  }

  return ok;
}

function validateSignUp() {
  var email = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
  var password = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
  var letters = /^[A-Za-z]+$/;
  var zipCode = /(^\d{5}$)|(^\d{5}-\d{4}$)/;
  var postalCode = /^[A-Za-z]\d[A-Za-z][ -]?\d[A-Za-z]\d$/;
  var ok = true;
  var p = document.getElementById("email").value;
  var vp = document.getElementById("repassword").value;
  var email = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
  var password = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;

  if (!p.match(email)) {
    alert("Invalid email format!");
    ok = false;
  }
  if (!ok) return false;

  p = document.getElementById("password").value;
  if (!p.match(password)) {
    alert(
      "Password must be between 6 to 20 characters and contains at least one number, one uppercase letter and one lowercase letter!"
    );
    ok = false;
  }

  if (!ok) return false;

  if (p != vp) {
    alert("Passwords do not match!");
    ok = false;
  }

  if (!ok) return false;

  p = document.getElementById("firstName").value;
  if (!p.match(letters)) {
    alert("First name must be letters only");
    ok = false;
  }

  if (!ok) return false;

  p = document.getElementById("lastName").value;
  if (!p.match(letters)) {
    alert("Last name must be letters only");
    ok = false;
  }

  if (!ok) return false;

  var aptNo = document.getElementById("aptNo").value;
  var streetNo = document.getElementById("streetNo").value;
  var streetName = document.getElementById("streetName").value;
  var city = document.getElementById("city").value;
  var province = document.getElementById("province").value;
  var postalCode = document.getElementById("postalCode").value;

  var nullAddress =
    aptNo == "" &&
    streetNo == "" &&
    streetName == "" &&
    city == "" &&
    province == "" &&
    postalCode == "";

  if (!nullAddress) {
    if (aptNo != "") {
      if (isNaN(aptNo) || aptNo < 0) {
        alert("Apartment number must be a positive number");
        ok = false;
      }
      if (!ok) return false;
    }

    if (isNaN(streetNo) || streetNo < 0 || streetNo == "") {
      alert("Street number must be a positive number");
      ok = false;
    }

    if (!ok) return false;

    if (!streetName.match(letters)) {
      alert("Street name must be letters only");
      ok = false;
    }

    if (!ok) return false;

    if (!city.match(letters)) {
      alert("City must be letters only");
      ok = false;
    }

    if (!ok) return false;

    if (!province.match(letters)) {
      alert("Province must be letters only");
      ok = false;
    }

    if (!ok) return false;

    if (p != "") {
      if (!postalCode.match(postalCode) && !postalCode.match(zipCode)) {
        alert("Invalid Zip/Postal Code!");
        ok = false;
      }
    }
    if (!ok) return false;
  }

  return ok;
}

function validateAddress() {
  var letters = /^[A-Za-z]+$/;
  var streetNameFormat = /^\s*([A-Za-z]+\s*)+\.?\s*$/;
  var cityFormat = /^\s*([A-Za-z]+\s*)+$/;
  var provinceFormat = /^\s*([A-Za-z]+\s*)+$/;
  var zipCodeFormat = /(^\s*\d{5}\s*$)|(^\s*\d{5}-\d{4}\s*$)/;
  var postalCodeFormat = /^\s*[A-Za-z]\d[A-Za-z]\s*\d[A-Za-z]\d\s*$/; 
  var ok = true;
  var aptNo = document.getElementById("aptNo").value;
  var streetNo = document.getElementById("streetNo").value;
  var streetName = document.getElementById("streetName").value;
  var city = document.getElementById("city").value;
  var province = document.getElementById("province").value;
  var postalCode = document.getElementById("postalCode").value;
 
  if (aptNo != "") {
    if (isNaN(aptNo) || aptNo < 0) {
      alert("Apartment number must be a positive number");
      ok = false;
    }
    if (!ok) return false;
  }

  if (streetNo == "" || isNaN(streetNo) || streetNo < 0) {
    alert("Street number must be a positive number");
    ok = false;
  }
  if (!ok) return false;

  if (streetName == "" || !streetName.match(streetNameFormat)) {
    alert("Street name must be letters only");
    ok = false;
  }
  if (!ok) return false;

  if (city == "" || !city.match(cityFormat)) {
    alert("City must be letters only");
    ok = false;
  }
  if (!ok) return false;

  if (province == "" || !province.match(provinceFormat)) {
    alert("Province must be letters only");
    ok = false;
  }
  if (!ok) return false;

  if (postalCode != "") {
    if (!postalCode.match(postalCodeFormat) && !postalCode.match(zipCodeFormat)) {
      alert("Invalid Zip/Postal Code!");
      ok = false;
    }
  }
  if (!ok) return false;

  return ok;
}

function validateCreditcard() {

	var nameFormat = /^([A-Za-z]+\s*)+/;
	var cardNumberFormat = /^(\d{4}[\s-]?){3}\d{4}$/;
	var cvcFormat = /^\d{3}$/;
	var monthFormat = /^[01]\d$/;
	var yearFormat = /^\d{2}$/;
	var nameOnCard = document.getElementById("nameOnCard").value;
	var cardNumber = document.getElementById("cardNumber").value;
	var cvc = document.getElementById("cvc").value;
	var month = document.getElementById("month").value;
	var year = document.getElementById("year").value;
	var ok = true;
	
	var empty = nameOnCard == "" &&
				cardNumber == "" &&
				cvc == "" &&
				month == "" &&
				year == "";
	
	if (!nameOnCard.match(nameFormat)) {
		alert("Name can only be letters and spaces");
		ok = false;
	}
	if (!ok) return false;
	
	if (!cardNumber.match(cardNumberFormat)) {
		alert("Card number must be 16 digits");
		ok = false;
	}
	if (!ok) return false;
	
	if (isNaN(cvc) || !cvc.match(cvcFormat)) {
		alert("CVC is a 3-digit code at the back of your card");
		ok = false;
	}
	if (!ok) return false;
	
	if (isNaN(month) || month < 1 || month > 12) {
		alert("Month must be a number from 1 to 12");
		ok = false;
	}
	if (!ok) return false;
	
	if (isNaN(year) || !year.match(yearFormat)) {
		alert("Year must be a number");
		ok = false;
	}
	if (!ok) return false;
	
	var inputDate = new Date(parseInt(year) + 2000, parseInt(month) - 1, 0);
	const today = new Date();
	
	if (inputDate <= today) {
		alert("Your card is expired");
		ok = false;
	}
	if (!ok) return false;
	
	return ok;
}

function handler(request) {
	if ((request.readyState == 4) && (request.status == 200)) {
		console.log("Request ready")
		return true;
	}
}

function sendOrder() {
	var selectedAddress = document.getElementById("address").value;
	
	if (selectedAddress != "" && !isNaN(selectedAddress) && validateCreditcard()) {
		var request = new XMLHttpRequest();
		var action = '';
	
		action += "checkout?placeOrder=true&address=" + selectedAddress;
		console.log(action)
		
		request.open("GET", (action), true);
		request.onreadystatechange = function() {
			handler(request);
		};
		request.send(null);
	}
	else {
		return false;		
	}

}

function validateOrder() {
	var ok = true;
	var selectedAddress = document.getElementById("address").value;
	
	if (selectedAddress == "") {
		alert("SEND TO Address not found");
		ok = false;
	}
	if (!ok) return false;
	
	if (isNaN(selectedAddress)) {
		alert("Invalid SEND TO address");
		ok = false;
	}
	if (!ok) return false;
	
	return validateCreditcard();
}