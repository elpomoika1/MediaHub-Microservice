const form = document.getElementById('uploadForm');
const moviesContainer = document.getElementById('moviesContainer');

function createMediaCard(media) {
    const card = document.createElement('div');
    card.className = 'media-card';

    const img = document.createElement('img');
    img.alt = media.title || '';

    // Разрешаем только относительные URL или http/https
    try {
        const url = new URL(media.imageUrl, window.location.origin);

        if (
            url.protocol === 'http:' ||
            url.protocol === 'https:' ||
            url.origin === window.location.origin
        ) {
            img.src = url.href;
        } else {
            img.src = '/images/no-image.png';
        }
    } catch {
        img.src = '/images/no-image.png';
    }

    const title = document.createElement('h3');
    title.textContent = media.title;

    const rating = document.createElement('p');
    rating.textContent = `Rating: ${media.rating}`;

    const votes = document.createElement('p');
    votes.textContent = `Votes: ${media.votes}`;

    card.appendChild(img);
    card.appendChild(title);
    card.appendChild(rating);
    card.appendChild(votes);

    return card;
}

function renderMovies(mediaList) {
    moviesContainer.replaceChildren();

    for (const media of mediaList) {
        moviesContainer.appendChild(createMediaCard(media));
    }
}

async function loadMovies() {
    try {
        const response = await fetch('/api/media/list');

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const data = await response.json();
        renderMovies(data);
    } catch (err) {
        console.error('Failed to load media:', err);
    }
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const fileInput = document.getElementById('file');
    const file = fileInput.files[0];

    if (!file) {
        alert('Select a file.');
        return;
    }

    const formData = new FormData();

    formData.append('file', file);
    formData.append(
        'data',
        new Blob(
            [JSON.stringify({
                title: document.getElementById('title').value,
                type: document.getElementById('type').value,
                genres: Array.from(
                    document.getElementById('genre').selectedOptions,
                    option => option.value
                )
            })],
            { type: 'application/json' }
        )
    );

    try {
        const response = await fetch('/api/media/upload', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        form.reset();
        await loadMovies();
    } catch (err) {
        console.error('Upload failed:', err);
    }
});

loadMovies();