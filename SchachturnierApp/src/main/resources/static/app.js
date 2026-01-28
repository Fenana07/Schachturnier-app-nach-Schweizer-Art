// Backend base URL required by the integration spec.
// If serving this UI from a different origin, ensure the Spring app enables CORS.
const API_BASE = "http://localhost:8080";

// REST helpers used across the Stitch-generated screens.
async function fetchJson(url, options = {}) {
  const response = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers ?? {})
    },
    mode: "cors",
    ...options
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `Request failed: ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

// --- API mapping helpers ---
// Tournament endpoints map to TournamentController/TournamentService.
const tournamentApi = {
  list: () => fetchJson(`${API_BASE}/tournaments`),
  create: (payload) =>
    fetchJson(`${API_BASE}/tournaments`, {
      method: "POST",
      body: JSON.stringify(payload)
    }),
  participants: (tournamentId) =>
    fetchJson(`${API_BASE}/tournaments/${tournamentId}/participants`),
  addParticipant: (tournamentId, payload) =>
    fetchJson(`${API_BASE}/tournaments/${tournamentId}/participants`, {
      method: "POST",
      body: JSON.stringify(payload)
    })
};

// Player endpoints map to PlayersController/PlayerService.
const playerApi = {
  list: () => fetchJson(`${API_BASE}/players`),
  create: (payload) =>
    fetchJson(`${API_BASE}/players`, {
      method: "POST",
      body: JSON.stringify(payload)
    }),
  remove: (playerId) =>
    fetchJson(`${API_BASE}/players/${playerId}`, { method: "DELETE" })
};

// Round/Match endpoints map to RoundController/MatchService.
const roundApi = {
  list: (tournamentId) =>
    fetchJson(`${API_BASE}/tournaments/${tournamentId}/rounds`),
  generateNext: (tournamentId) =>
    fetchJson(`${API_BASE}/tournaments/${tournamentId}/rounds/next`, {
      method: "POST"
    })
};

const matchApi = {
  updateResult: (matchId, payload) =>
    fetchJson(`${API_BASE}/matches/${matchId}/result`, {
      method: "PUT",
      body: JSON.stringify(payload)
    })
};

// Standings endpoint maps to StandingService.
const standingApi = {
  list: (tournamentId) =>
    fetchJson(`${API_BASE}/tournaments/${tournamentId}/standings`)
};

const resultOptions = [
  { value: "ONE_ZERO", label: "1-0" },
  { value: "ZERO_ONE", label: "0-1" },
  { value: "HALF_HALF", label: "1/2-1/2" },
  { value: "F1_0", label: "F1-0" },
  { value: "ZERO_F1", label: "0-F1" },
  { value: "BYE", label: "BYE" },
  { value: "ZERO_ZERO", label: "0-0" }
];

const toast = document.getElementById("toast");
const state = {
  tournaments: [],
  participantsByTournament: new Map(),
  roundsByTournament: new Map()
};

function showToast(message) {
  toast.textContent = message;
  toast.classList.add("show");
  setTimeout(() => toast.classList.remove("show"), 2500);
}

function setActiveSection(sectionId) {
  document.querySelectorAll(".section").forEach((section) => {
    section.classList.toggle("is-active", section.id === sectionId);
  });
  document.querySelectorAll(".app-nav button").forEach((button) => {
    button.classList.toggle("active", button.dataset.section === sectionId);
  });
}

async function loadTournaments() {
  const tournaments = await tournamentApi.list();
  state.tournaments = tournaments;
  renderTournamentCards(tournaments);
  hydrateTournamentSelects(tournaments);
  const tournamentId = document.getElementById("pairings-tournament")?.value;
  updateGenerateButtonState(tournamentId);
}

function renderTournamentCards(tournaments) {
  const list = document.getElementById("tournament-list");
  list.innerHTML = "";

  tournaments.forEach((tournament) => {
    const card = document.createElement("div");
    card.className = "card";
    card.innerHTML = `
      <h2>${tournament.name}</h2>
      <p><span class="badge">${tournament.roundsCount ?? "?"} rounds</span></p>
      <p>Players: ${tournament.playersCount ?? "-"}</p>
      <p>Rounds played: ${tournament.roundsPlayed ?? "-"}</p>
      <button data-id="${tournament.id}" class="open-tournament">Open Tournament</button>
    `;
    list.appendChild(card);
  });
}

function hydrateTournamentSelects(tournaments) {
  const selects = [
    document.getElementById("participant-tournament"),
    document.getElementById("pairings-tournament"),
    document.getElementById("standings-tournament")
  ];

  selects.forEach((select) => {
    if (!select) return;
    select.innerHTML = "";
    if (tournaments.length === 0) {
      const option = document.createElement("option");
      option.value = "";
      option.textContent = "No tournaments available";
      select.appendChild(option);
      return;
    }
    tournaments.forEach((tournament) => {
      const option = document.createElement("option");
      option.value = tournament.id;
      option.textContent = tournament.name;
      select.appendChild(option);
    });
  });
}

async function loadPlayers() {
  const players = await playerApi.list();
  renderPlayers(players);
  hydratePlayerSelect(players);
}

function renderPlayers(players) {
  const list = document.getElementById("player-list");
  list.innerHTML = "";
  players.forEach((player) => {
    const rating = player.rating ?? player.initialRating ?? "-";
    const row = document.createElement("div");
    row.className = "list-row";
    row.innerHTML = `
      <div>
        <strong>${player.firstName} ${player.lastName}</strong>
        <div class="muted">Rating: ${rating}</div>
      </div>
      <button data-id="${player.id}" class="remove-player">Remove</button>
    `;
    list.appendChild(row);
  });
}

function hydratePlayerSelect(players) {
  const select = document.getElementById("participant-player");
  select.innerHTML = "";
  players.forEach((player) => {
    const option = document.createElement("option");
    option.value = player.id;
    option.textContent = `${player.firstName} ${player.lastName}`;
    select.appendChild(option);
  });
}

async function loadParticipants(tournamentId) {
  const participants = await tournamentApi.participants(tournamentId);
  state.participantsByTournament.set(String(tournamentId), participants);
  const list = document.getElementById("participants-list");
  list.innerHTML = "";
  participants.forEach((participant) => {
    const row = document.createElement("div");
    row.className = "list-row";
    row.innerHTML = `
      <div>
        <strong>${participant.playerName}</strong>
        <div class="muted">Seed: ${participant.seedNo ?? "-"}</div>
      </div>
      <span class="badge">${participant.initialRating ?? "-"}</span>
    `;
    list.appendChild(row);
  });
}

async function loadRounds(tournamentId) {
  const rounds = await roundApi.list(tournamentId);
  state.roundsByTournament.set(String(tournamentId), rounds);
  const container = document.getElementById("rounds-list");
  container.innerHTML = "";

  rounds.forEach((round) => {
    const roundBlock = document.createElement("div");
    roundBlock.className = "stack";
    roundBlock.innerHTML = `<h2>Round ${round.number}</h2>`;

    round.matches.forEach((match) => {
      const row = document.createElement("div");
      row.className = "list-row";
      const options = resultOptions
        .map((option) =>
          `<option value="${option.value}" ${option.value === match.resultCode ? "selected" : ""}>${option.label}</option>`
        )
        .join("");
      row.innerHTML = `
        <div>
          <strong>Board ${match.boardNo}</strong>
          <div>${match.whitePlayer ?? "BYE"} vs ${match.blackPlayer ?? "BYE"}</div>
        </div>
        <select data-match-id="${match.id}" data-current-result="${match.resultCode}" class="result-select">
          ${options}
        </select>
      `;
      roundBlock.appendChild(row);
    });

    container.appendChild(roundBlock);
  });
}

function updateGenerateButtonState(tournamentId) {
  const button = document.getElementById("generate-round");
  if (!tournamentId) {
    button.disabled = true;
    return;
  }
  const participants = state.participantsByTournament.get(String(tournamentId)) ?? [];
  const rounds = state.roundsByTournament.get(String(tournamentId)) ?? [];
  const tournament = state.tournaments.find((item) => String(item.id) === String(tournamentId));
  const maxRounds = tournament?.roundsCount ?? null;

  const noPlayers = participants.length === 0;
  const roundsComplete = Number.isFinite(maxRounds) && rounds.length >= maxRounds;
  button.disabled = noPlayers || roundsComplete;
}

async function loadStandings(tournamentId) {
  const standings = await standingApi.list(tournamentId);
  const container = document.getElementById("standings-list");
  container.innerHTML = "";

  standings.forEach((standing, index) => {
    const row = document.createElement("div");
    row.className = "list-row";
    row.innerHTML = `
      <div>
        <strong>#${index + 1} ${standing.playerName}</strong>
        <div class="muted">Points: ${standing.points ?? 0}</div>
      </div>
      <div class="badge">TB1: ${standing.tieBreaker1 ?? "-"}</div>
      <div class="badge">TB2: ${standing.tieBreaker2 ?? "-"}</div>
      <div class="badge">TB3: ${standing.tieBreaker3 ?? "-"}</div>
    `;
    container.appendChild(row);
  });
}

function wireNavigation() {
  document.querySelectorAll(".app-nav button").forEach((button) => {
    button.addEventListener("click", () => {
      setActiveSection(button.dataset.section);
    });
  });
}

function wireForms() {
  document.getElementById("tournament-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    const tieBreakers = formData.getAll("tiebreakers");

    // Backend mapping: POST /tournaments with name + roundsCount.
    // Tie-breakers are passed as an ordered array of TieBreakerType.
    try {
      await tournamentApi.create({
        name: formData.get("name"),
        roundsCount: Number(formData.get("rounds")),
        numberOfRounds: Number(formData.get("rounds")),
        tieBreakers
      });
      showToast("Tournament created.");
      event.target.reset();
      await loadTournaments();
    } catch (error) {
      console.error(error);
      showToast("Failed to create tournament.");
    }
  });

  document.getElementById("add-participant-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);
    const tournamentId = formData.get("tournament");

    // Backend mapping: POST /tournaments/{id}/participants for Participation creation.
    try {
      await tournamentApi.addParticipant(tournamentId, {
        playerId: formData.get("player"),
        seedNo: Number(formData.get("seed")) || null,
        initialRating: Number(formData.get("rating")) || null
      });
      showToast("Participant added.");
      await loadParticipants(tournamentId);
      updateGenerateButtonState(tournamentId);
    } catch (error) {
      console.error(error);
      showToast("Failed to add participant.");
    }
  });

  document.getElementById("player-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const formData = new FormData(event.target);

    // Backend mapping: POST /players for Player creation.
    try {
      await playerApi.create({
        firstName: formData.get("firstName"),
        lastName: formData.get("lastName"),
        rating: Number(formData.get("rating")) || null
      });
      showToast("Player created.");
      await loadPlayers();
    } catch (error) {
      console.error(error);
      showToast("Failed to create player.");
    }
  });
}

function wireActions() {
  document.getElementById("tournament-list").addEventListener("click", async (event) => {
    const button = event.target.closest(".open-tournament");
    if (!button) return;

    const tournamentId = button.dataset.id;
    setActiveSection("pairings");
    document.getElementById("pairings-tournament").value = tournamentId;
    await loadRounds(tournamentId);
  });

  document.getElementById("player-list").addEventListener("click", async (event) => {
    const button = event.target.closest(".remove-player");
    if (!button) return;

    try {
      await playerApi.remove(button.dataset.id);
      showToast("Player removed.");
      await loadPlayers();
      const tournamentId = document.getElementById("participant-tournament").value;
      if (tournamentId) {
        await loadParticipants(tournamentId);
        updateGenerateButtonState(tournamentId);
      }
    } catch (error) {
      console.error(error);
      showToast("Failed to remove player.");
    }
  });

  document.getElementById("pairings-tournament").addEventListener("change", async (event) => {
    const tournamentId = event.target.value;
    if (!tournamentId) {
      updateGenerateButtonState(tournamentId);
      document.getElementById("rounds-list").innerHTML = "";
      return;
    }
    await Promise.all([loadRounds(tournamentId), loadParticipants(tournamentId)]);
    updateGenerateButtonState(tournamentId);
  });

  document.getElementById("standings-tournament").addEventListener("change", async (event) => {
    await loadStandings(event.target.value);
  });

  document.getElementById("generate-round").addEventListener("click", async () => {
    const tournamentId = document.getElementById("pairings-tournament").value;
    if (!tournamentId) {
      showToast("Select a tournament first.");
      return;
    }

    // Backend mapping: POST /tournaments/{id}/rounds/next triggers PairingService.
    try {
      await roundApi.generateNext(tournamentId);
      showToast("Round generated.");
      await loadRounds(tournamentId);
      updateGenerateButtonState(tournamentId);
    } catch (error) {
      console.error(error);
      showToast("Failed to generate round.");
    }
  });

  document.getElementById("rounds-list").addEventListener("change", async (event) => {
    const select = event.target.closest(".result-select");
    if (!select) return;

    const matchId = select.dataset.matchId;

    // Backend mapping: PUT /matches/{id}/result updates Match.resultCode.
    const previousValue = select.dataset.currentResult;
    if (previousValue && previousValue !== select.value) {
      const confirmed = window.confirm("Save the new match result?");
      if (!confirmed) {
        select.value = previousValue;
        return;
      }
    }
    try {
      await matchApi.updateResult(matchId, {
        resultCode: select.value
      });
      select.dataset.currentResult = select.value;
      const tournamentId = document.getElementById("pairings-tournament").value;
      showToast("Result saved.");
      await loadStandings(tournamentId);
    } catch (error) {
      console.error(error);
      showToast("Failed to save result.");
    }
  });
}

async function bootstrap() {
  wireNavigation();
  wireForms();
  wireActions();

  await Promise.all([loadTournaments(), loadPlayers()]);

  const defaultTournamentId = document.getElementById("pairings-tournament").value;
  if (defaultTournamentId) {
    await Promise.all([
      loadParticipants(defaultTournamentId),
      loadRounds(defaultTournamentId),
      loadStandings(defaultTournamentId)
    ]);
    updateGenerateButtonState(defaultTournamentId);
  }

  document.getElementById("participant-tournament").addEventListener("change", async (event) => {
    const tournamentId = event.target.value;
    await loadParticipants(tournamentId);
    updateGenerateButtonState(tournamentId);
  });

  document.getElementById("create-tournament").addEventListener("click", () => {
    setActiveSection("setup");
    document.querySelector("#tournament-form input[name=\"name\"]")?.focus();
  });
}

bootstrap().catch((error) => {
  console.error(error);
  showToast("Failed to load data. Check API endpoints.");
});
