const key = 'luigis-workout-demo-v1';
const fresh = () => ({ water: 0, meals: [], workouts: [], weights: [] });
let state = fresh();
try { state = JSON.parse(localStorage.getItem(key)) ?? state; } catch { /* Storage may be unavailable. */ }

const days = {
  Push: ['Dumbbell bench press', 'Overhead press', 'Lateral raise', 'Triceps extension'],
  Pull: ['Dumbbell row', 'Reverse fly', 'Dumbbell curl'],
  Legs: ['Goblet squat', 'Romanian deadlift', 'Calf raise'],
  'Shoulders & Arms': ['Cross curl', 'Triceps kickback', 'Hammer press', 'Lateral raise'],
  'Full Body': ['Goblet squat', 'Dumbbell row', 'Bike intervals']
};
const screen = document.querySelector('#screen');
const notice = document.querySelector('#notice');
let tab = 'Home';
let day = 'Push';
let completed = new Set();
let started = null;
let seconds = 0;

function save(message) {
  try {
    localStorage.setItem(key, JSON.stringify(state));
    notice.textContent = message;
  } catch {
    notice.textContent = message + ' Browser storage is unavailable for this session.';
  }
  render();
}

function list(id, entries, empty) {
  const node = document.getElementById(id);
  for (const value of entries.length ? entries : [empty]) {
    const item = document.createElement('li');
    item.textContent = value;
    node.append(item);
  }
}

function elapsed() {
  return seconds + (started === null ? 0 : Math.floor((Date.now() - started) / 1000));
}

function refreshTimer() {
  const clock = document.getElementById('clock');
  const time = elapsed();
  if (clock) clock.textContent = [Math.floor(time / 60), time % 60].map(n => String(n).padStart(2, '0')).join(':');
}

function render() {
  for (const button of document.querySelectorAll('[data-tab]')) {
    if (button.dataset.tab === tab) button.setAttribute('aria-current', 'page');
    else button.removeAttribute('aria-current');
  }
  if (tab === 'Home') {
    const calories = state.meals.reduce((total, meal) => total + meal.calories, 0);
    screen.innerHTML = `<h2>Home</h2><article><h3>Nutrition summary</h3><p>${calories} kcal across ${state.meals.length} logged meals</p><button data-action="Meals">Log meal</button></article>
      <article><h3>Water tracker</h3><p>${state.water} of 8 glasses</p><button data-action="less" aria-label="Remove a glass" ${state.water === 0 ? 'disabled' : ''}>−</button> <button data-action="more" aria-label="Add a glass">+</button></article>
      <article><h3>Training plan</h3><p>Choose a workout, track exercises and save your session.</p><button data-action="Workout">View workout</button></article>`;
  } else if (tab === 'Meals') {
    screen.innerHTML = `<h2>Meals</h2><form id="meal"><label>Meal type<select name="type"><option>Breakfast</option><option>Lunch</option><option>Dinner</option><option>Snack</option><option>Drink</option></select></label>
      <label>Food name<input name="name" required maxlength="80"></label><label>Calories (kcal)<input type="number" name="calories" min="0" max="20000" required></label>
      <button>Save meal</button> <button type="button" class="secondary" data-action="sample">Try a sample</button></form><article><h3>Meal log</h3><ul id="meals"></ul></article>`;
    list('meals', state.meals.map(m => `${m.type} · ${m.name} · ${m.calories} kcal`).reverse(), 'No meals logged.');
  } else if (tab === 'Workout') {
    const active = started !== null;
    screen.innerHTML = `<h2>Workout</h2><article><label>Training day<select id="day" ${active || seconds || completed.size ? 'disabled' : ''}>${Object.keys(days).map(name => `<option ${day === name ? 'selected' : ''}>${name}</option>`).join('')}</select></label>
      <p>Time <strong id="clock"></strong> · ${completed.size} / ${days[day].length} exercises</p>
      <button data-action="timer">${active ? 'Pause' : seconds ? 'Resume' : 'Start'}</button>
      ${days[day].map((name, i) => `<label class="exercise"><input type="checkbox" data-exercise="${i}" ${completed.has(i) ? 'checked' : ''}> ${name}</label>`).join('')}
      <button data-action="finish" ${completed.size ? '' : 'disabled'}>Save workout</button> <button class="secondary" data-action="clear-workout">Clear session</button></article>`;
    refreshTimer();
  } else {
    screen.innerHTML = `<h2>Progress</h2><form id="weight"><h3>Log weight</h3><label>Weight (kg)<input type="number" name="kg" min="30" max="300" step="0.1" required></label><button>Save weight</button></form>
      <article><h3>Weight history</h3><ul id="weights"></ul></article><article><h3>Workout history</h3><ul id="workouts"></ul></article>`;
    list('weights', state.weights.map(w => `${w} kg`).reverse(), 'No weights logged.');
    list('workouts', state.workouts.map(w => `${w.day} · ${w.count} exercises · ${w.seconds}s`).reverse(), 'No workouts logged.');
  }
}

function clearWorkout() {
  completed.clear();
  started = null;
  seconds = 0;
}

document.addEventListener('click', event => {
  const button = event.target.closest('button');
  if (!button) return;
  const action = button.dataset.tab ?? button.dataset.action;
  if (button.id === 'reset') {
    if (!window.confirm('Clear the demo logs and restart?')) return;
    state = fresh();
    clearWorkout();
    tab = 'Home';
    save('Demo reset.');
  } else if (['Home', 'Meals', 'Workout', 'Progress'].includes(action)) {
    tab = action;
    notice.textContent = '';
    render();
  } else if (action === 'less' || action === 'more') {
    state.water = Math.max(0, state.water + (action === 'more' ? 1 : -1));
    save('Water count updated.');
  } else if (action === 'timer') {
    if (started === null) started = Date.now();
    else {
      seconds = elapsed();
      started = null;
    }
    render();
  } else if (action === 'finish' && completed.size) {
    state.workouts.push({ day, count: completed.size, seconds: elapsed() });
    clearWorkout();
    tab = 'Progress';
    save('Workout saved.');
  } else if (action === 'clear-workout') {
    clearWorkout();
    render();
  } else if (action === 'sample') {
    const form = document.getElementById('meal');
    form.elements.type.value = 'Breakfast';
    form.elements.name.value = 'Greek yogurt and oats';
    form.elements.calories.value = '320';
  }
});

screen.addEventListener('change', event => {
  const field = event.target;
  if (field.id === 'day') day = field.value;
  else if ('exercise' in field.dataset) {
    const index = Number(field.dataset.exercise);
    if (field.checked) completed.add(index);
    else completed.delete(index);
    render();
    screen.querySelector(`[data-exercise="${index}"]`).focus();
  }
});

screen.addEventListener('submit', event => {
  event.preventDefault();
  const form = event.target;
  const data = new FormData(form);
  if (form.id === 'meal') {
    const name = data.get('name').trim();
    if (!name) {
      notice.textContent = 'Enter a food name.';
      form.elements.name.focus();
      return;
    }
    state.meals.push({ name, type: data.get('type'), calories: Number(data.get('calories')) });
    save('Meal saved.');
  } else if (form.id === 'weight') {
    state.weights.push(Number(data.get('kg')));
    save('Weight saved.');
  }
});

render();
setInterval(refreshTimer, 250);
