//let currentSessionId = "local-session-" + Math.random().toString(36).substring(2, 9);
//
//async function sendUssdRequest(userInput = "") {
//    const display = document.getElementById("display");
//    const inputField = document.getElementById("inputField");
//
//    display.innerHTML = "Processing...";
//
//    const formData = new FormData();
//    formData.append("sessionId", currentSessionId);
//    formData.append("phoneNumber", "251911223344");
//    formData.append("text", userInput.trim());
//
//    try {
//        const response = await fetch("/ussd", {
//            method: "POST",
//            body: formData
//        });
//
//        const result = await response.text();
//
//        if (result.startsWith("CON ")) {
//            display.innerHTML = result.substring(4).replace(/\n/g, "<br>");
//        }
//        else if (result.startsWith("END ")) {
//            display.innerHTML = `<span class="text-green-400">${result.substring(4)}</span><br><br>` +
//                                `<small class="text-gray-500">Session ended • Dial again to start new session</small>`;
//
//            // Reset session for next full interaction
//            currentSessionId = "local-session-" + Math.random().toString(36).substring(2, 9);
//        }
//        else {
//            display.innerHTML = result;
//        }
//
//    } catch (err) {
//        display.innerHTML = "Connection error.<br>Make sure Spring Boot is running on port 8080.";
//        console.error(err);
//    }
//
//    // 🔥 AUTO CLEAR the input field after every send
//    inputField.value = "";
//}
//
//// Main dial/send function
//function dial() {
//    const inputField = document.getElementById("inputField");
//    const userInput = inputField.value.trim();
//
//    sendUssdRequest(userInput);
//}
//
//// Clear button
//function clearInput() {
//    document.getElementById("inputField").value = "";
//    document.getElementById("display").innerHTML = "Input cleared.<br>Enter *123# or leave empty and press DIAL";
//}
//
//// Allow pressing Enter key in input field
//document.addEventListener("DOMContentLoaded", function() {
//    const inputField = document.getElementById("inputField");
//
//    inputField.addEventListener("keypress", function(event) {
//        if (event.key === "Enter") {
//            event.preventDefault();
//            dial();
//        }
//    });
//});
let currentSessionId = "local-session-" + Math.random().toString(36).substring(2, 9);
let accumulatedText = "";

async function sendUssdRequest(currentInput = "") {
    const display = document.getElementById("display");

    if (currentInput.trim() !== "") {
        accumulatedText = accumulatedText ? accumulatedText + "*" + currentInput.trim() : currentInput.trim();
    }

    display.innerHTML = "Processing...";

    const formData = new FormData();
    formData.append("sessionId", currentSessionId);
    formData.append("phoneNumber", "251911223344");
    formData.append("text", accumulatedText);

    try {
        const response = await fetch("/ussd", {
            method: "POST",
            body: formData
        });

        const result = await response.text();

        let displayText = result;

        if (result.startsWith("CON ")) {
            displayText = result.substring(4);
        } else if (result.startsWith("END ")) {
            displayText = result.substring(4);
        }

        // Replace \n with <br> for proper line breaks
        display.innerHTML = displayText.replace(/\n/g, "<br>");

    } catch (err) {
        display.innerHTML = "Connection error.<br>Is Spring Boot running?";
        console.error(err);
    }

    // Auto clear input
    document.getElementById("inputField").value = "";
}

// Append from keypad
function appendToInput(digit) {
    document.getElementById("inputField").value += digit;
}

function dial() {
    const inputField = document.getElementById("inputField");
    sendUssdRequest(inputField.value);
}

function clearInput() {
    document.getElementById("inputField").value = "";
    accumulatedText = "";
    document.getElementById("display").innerHTML = "Session cleared.<br>Press DIAL to start again";
}

// Enter key support
document.addEventListener("DOMContentLoaded", () => {
    const inputField = document.getElementById("inputField");
    inputField.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            dial();
        }
    });
});