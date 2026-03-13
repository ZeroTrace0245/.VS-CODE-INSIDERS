// Character data with descriptions
const characters = {
    garfield: {
        name: "Garfield",
        description: "The lazy, orange tabby cat who loves lasagna and hates Mondays. Known for his witty humor and cynical attitude.",
        image: "images/garfield.jpg"
    },
    odie: {
        name: "Odie",
        description: "Garfield's loyal dog friend. Energetic, simple-minded, and always eager to play and have fun.",
        image: "images/odie.jpg"
    },
    nermal: {
        name: "Nermal",
        description: "The tiny, cute kitten from Abu Dhabi. Often mocked by Garfield for being too adorable and annoying.",
        image: "images/nermal.jpg"
    },
    pooky: {
        name: "Pooky",
        description: "Garfield's beloved teddy bear and loyal companion. A source of comfort and emotional support.",
        image: "images/pooky.jpg"
    },
    jon: {
        name: "Jon Arbuckle",
        description: "Garfield's owner and the primary source of humor. A well-meaning but often clueless human.",
        image: "images/jon.jpg"
    }
};

const characterArray = Object.keys(characters);
let currentIndex = 0;

function updateDisplay() {
    const currentCharacter = characterArray[currentIndex];
    const character = characters[currentCharacter];

    // Update select dropdown
    document.getElementById("character").value = currentCharacter;

    // Update image
    document.getElementById("characterImage").src = character.image;
    document.getElementById("characterImage").alt = character.name;

    // Update character info
    document.getElementById("characterName").textContent = character.name;
    document.getElementById("characterDescription").textContent = character.description;

    // Update counter
    document.getElementById("counter").textContent = `${currentIndex + 1} of ${characterArray.length}`;
}

function changeImage() {
    const character = document.getElementById("character").value;
    currentIndex = characterArray.indexOf(character);
    updateDisplay();
}

function nextCharacter() {
    currentIndex = (currentIndex + 1) % characterArray.length;
    updateDisplay();
}

function previousCharacter() {
    currentIndex = (currentIndex - 1 + characterArray.length) % characterArray.length;
    updateDisplay();
}

// Initialize display
updateDisplay();