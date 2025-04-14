function createConfetti() {
    const colors = ['#FF6B6B', '#4CC9F0', '#F7B801', '#8338EC', '#06D6A0'];
    const shapes = ['square', 'circle', 'triangle'];

    for (let i = 0; i < 60; i++) {
        const confetti = document.createElement('div');
        confetti.classList.add('confetti');

        const shape = shapes[Math.floor(Math.random() * shapes.length)];
        const size = Math.random() * 10 + 5;

        confetti.style.left = Math.random() * 100 + 'vw';
        confetti.style.width = size + 'px';
        confetti.style.height = size + 'px';
        confetti.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
        confetti.style.animationDuration = Math.random() * 3 + 3 + 's';
        confetti.style.animationDelay = Math.random() * 5 + 's';

        if (shape === 'circle') {
            confetti.style.borderRadius = '50%';
        } else if (shape === 'triangle') {
            confetti.style.backgroundColor = 'transparent';
            confetti.style.width = '0';
            confetti.style.height = '0';
            confetti.style.borderLeft = size / 2 + 'px solid transparent';
            confetti.style.borderRight = size / 2 + 'px solid transparent';
            confetti.style.borderBottom = size + 'px solid ' + colors[Math.floor(Math.random() * colors.length)];
        }

        document.body.appendChild(confetti);
    }
}

function createStars() {
    for (let i = 0; i < 40; i++) {
        const star = document.createElement('div');
        star.classList.add('star');

        star.style.position = 'absolute';
        star.style.width = '2px';
        star.style.height = '2px';
        star.style.backgroundColor = 'white';
        star.style.borderRadius = '50%';
        star.style.opacity = Math.random() * 0.5 + 0.3;
        star.style.left = Math.random() * 100 + 'vw';
        star.style.top = Math.random() * 100 + 'vh';
        star.style.boxShadow = '0 0 4px white';
        star.style.animation = 'twinkle ' + (Math.random() * 2 + 2) + 's infinite alternate';

        document.body.appendChild(star);
    }
}

const style = document.createElement('style');
style.type = 'text/css';
style.innerHTML = `
    @keyframes twinkle {
    0% { opacity: 0.3; }
    100% { opacity: 1; }
}
    `;
document.head.appendChild(style);

window.onload = function () {
    createConfetti();
    createStars();

    const container = document.querySelector('.container');
    container.addEventListener('mousemove', function (e) {
        const x = e.clientX / window.innerWidth;
        const y = e.clientY / window.innerHeight;

        // Calculate rotation based on mouse position
        const rotateX = (y - 0.5) * 5;
        const rotateY = (x - 0.5) * -5;

        container.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
    });

    container.addEventListener('mouseleave', function () {
        container.style.transform = 'perspective(1000px) rotateX(0) rotateY(0)';
        container.style.transition = 'transform 0.5s ease';
    });
};