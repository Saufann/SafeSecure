package com.example.kriptotugas1

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ln
import kotlin.math.min
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var authGate: View
    private lateinit var pageContainer: View
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var pageHome: View
    private lateinit var pageVault: View
    private lateinit var pageDocument: View
    private lateinit var pageSignature: View
    private lateinit var pageCertificate: View

    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordInput: TextInputEditText
    private lateinit var progressStrength: LinearProgressIndicator
    private lateinit var tvStrengthLabel: TextView
    private lateinit var tvStrengthScore: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var tvAnalyzedAt: TextView
    private lateinit var tvLengthValue: TextView
    private lateinit var tvCompositionValue: TextView
    private lateinit var tvEntropyValue: TextView
    private lateinit var tvGuessTimeValue: TextView
    private lateinit var tvHashValue: TextView
    private lateinit var tvRuleLength: TextView
    private lateinit var tvRuleUppercase: TextView
    private lateinit var tvRuleLowercase: TextView
    private lateinit var tvRuleDigit: TextView
    private lateinit var tvRuleSymbol: TextView
    private lateinit var tvRulePattern: TextView

    private lateinit var authEmailInput: TextInputEditText
    private lateinit var authPasswordInput: TextInputEditText
    private lateinit var tvAuthStatus: TextView

    private lateinit var vaultServiceInput: TextInputEditText
    private lateinit var vaultUsernameInput: TextInputEditText
    private lateinit var vaultPasswordInput: TextInputEditText
    private lateinit var tvVaultSummary: TextView
    private lateinit var tvVaultEntries: TextView

    private lateinit var documentNameInput: TextInputEditText
    private lateinit var documentContentInput: TextInputEditText
    private lateinit var documentReferenceHashInput: TextInputEditText
    private lateinit var tvDocumentStatus: TextView
    private lateinit var tvDocumentHash: TextView
    private lateinit var tvDocumentDetails: TextView

    private lateinit var signerNameInput: TextInputEditText
    private lateinit var signatureDocumentInput: TextInputEditText
    private lateinit var signatureVerifierInput: TextInputEditText
    private lateinit var tvSignatureStatus: TextView
    private lateinit var tvGeneratedSignature: TextView
    private lateinit var tvSignatureDetails: TextView

    private lateinit var certificateNameInput: TextInputEditText
    private lateinit var certificateIdInput: TextInputEditText
    private lateinit var certificateEventInput: TextInputEditText
    private lateinit var certificateQrImage: ImageView
    private lateinit var tvCertificateHash: TextView
    private lateinit var tvCertificatePayload: TextView

    private var firebaseAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    private var firebaseReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
        initializeFirebaseServices()
        configureActions()
        resetDashboard()
        resetFeaturePages()
        updateAuthGate()
    }

    override fun onStart() {
        super.onStart()
        if (::authGate.isInitialized) {
            updateAuthStatus()
            updateAuthGate()
        }
    }

    private fun bindViews() {
        authGate = findViewById(R.id.authGate)
        pageContainer = findViewById(R.id.pageContainer)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        pageHome = findViewById(R.id.pageHome)
        pageVault = findViewById(R.id.pageVault)
        pageDocument = findViewById(R.id.pageDocument)
        pageSignature = findViewById(R.id.pageSignature)
        pageCertificate = findViewById(R.id.pageCertificate)

        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        passwordInput = findViewById(R.id.etPassword)
        progressStrength = findViewById(R.id.progressStrength)
        tvStrengthLabel = findViewById(R.id.tvStrengthLabel)
        tvStrengthScore = findViewById(R.id.tvStrengthScore)
        tvFeedback = findViewById(R.id.tvFeedback)
        tvAnalyzedAt = findViewById(R.id.tvAnalyzedAt)
        tvLengthValue = findViewById(R.id.tvLengthValue)
        tvCompositionValue = findViewById(R.id.tvCompositionValue)
        tvEntropyValue = findViewById(R.id.tvEntropyValue)
        tvGuessTimeValue = findViewById(R.id.tvGuessTimeValue)
        tvHashValue = findViewById(R.id.tvHashValue)
        tvRuleLength = findViewById(R.id.tvRuleLength)
        tvRuleUppercase = findViewById(R.id.tvRuleUppercase)
        tvRuleLowercase = findViewById(R.id.tvRuleLowercase)
        tvRuleDigit = findViewById(R.id.tvRuleDigit)
        tvRuleSymbol = findViewById(R.id.tvRuleSymbol)
        tvRulePattern = findViewById(R.id.tvRulePattern)

        authEmailInput = findViewById(R.id.etAuthEmail)
        authPasswordInput = findViewById(R.id.etAuthPassword)
        tvAuthStatus = findViewById(R.id.tvAuthStatus)

        vaultServiceInput = findViewById(R.id.etVaultService)
        vaultUsernameInput = findViewById(R.id.etVaultUsername)
        vaultPasswordInput = findViewById(R.id.etVaultPassword)
        tvVaultSummary = findViewById(R.id.tvVaultSummary)
        tvVaultEntries = findViewById(R.id.tvVaultEntries)

        documentNameInput = findViewById(R.id.etDocumentName)
        documentContentInput = findViewById(R.id.etDocumentContent)
        documentReferenceHashInput = findViewById(R.id.etDocumentReferenceHash)
        tvDocumentStatus = findViewById(R.id.tvDocumentStatus)
        tvDocumentHash = findViewById(R.id.tvDocumentHash)
        tvDocumentDetails = findViewById(R.id.tvDocumentDetails)

        signerNameInput = findViewById(R.id.etSignerName)
        signatureDocumentInput = findViewById(R.id.etSignatureDocument)
        signatureVerifierInput = findViewById(R.id.etSignatureVerifier)
        tvSignatureStatus = findViewById(R.id.tvSignatureStatus)
        tvGeneratedSignature = findViewById(R.id.tvGeneratedSignature)
        tvSignatureDetails = findViewById(R.id.tvSignatureDetails)

        certificateNameInput = findViewById(R.id.etCertificateName)
        certificateIdInput = findViewById(R.id.etCertificateId)
        certificateEventInput = findViewById(R.id.etCertificateEvent)
        certificateQrImage = findViewById(R.id.ivCertificateQr)
        tvCertificateHash = findViewById(R.id.tvCertificateHash)
        tvCertificatePayload = findViewById(R.id.tvCertificatePayload)
    }

    private fun initializeFirebaseServices() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
            firebaseReady = FirebaseApp.getApps(this).isNotEmpty()
            if (firebaseReady) {
                firebaseAuth = FirebaseAuth.getInstance()
                firestore = FirebaseFirestore.getInstance()
            }
        } catch (exception: Exception) {
            firebaseReady = false
            firebaseAuth = null
            firestore = null
        }
        updateAuthStatus()
    }

    private fun configureActions() {
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            showPage(menuItem.itemId)
            true
        }

        findViewById<MaterialButton>(R.id.btnAuthLogin).setOnClickListener {
            loginFirebaseUser()
        }
        findViewById<MaterialButton>(R.id.btnAuthRegister).setOnClickListener {
            registerFirebaseUser()
        }
        findViewById<MaterialButton>(R.id.btnAuthLogout).setOnClickListener {
            signOutFirebaseUser()
        }

        findViewById<MaterialButton>(R.id.btnAnalyze).setOnClickListener {
            analyzePassword()
        }

        findViewById<MaterialButton>(R.id.btnClear).setOnClickListener {
            passwordInput.text?.clear()
            passwordInputLayout.error = null
            resetDashboard()
        }

        passwordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                analyzePassword()
                true
            } else {
                false
            }
        }

        findViewById<MaterialButton>(R.id.btnVaultSave).setOnClickListener {
            saveVaultEntry()
        }
        findViewById<MaterialButton>(R.id.btnVaultClear).setOnClickListener {
            clearVaultForm()
        }
        findViewById<MaterialButton>(R.id.btnDocumentCheck).setOnClickListener {
            checkDocumentAuthenticity()
        }
        findViewById<MaterialButton>(R.id.btnGenerateSignature).setOnClickListener {
            generateDigitalSignature()
        }
        findViewById<MaterialButton>(R.id.btnVerifySignature).setOnClickListener {
            verifyDigitalSignature()
        }
        findViewById<MaterialButton>(R.id.btnGenerateCertificate).setOnClickListener {
            generateCertificateHashQr()
        }
    }

    private fun showPage(menuItemId: Int) {
        val selectedPage = when (menuItemId) {
            R.id.nav_vault -> pageVault
            R.id.nav_document -> pageDocument
            R.id.nav_signature -> pageSignature
            R.id.nav_certificate -> pageCertificate
            else -> pageHome
        }

        listOf(pageHome, pageVault, pageDocument, pageSignature, pageCertificate).forEach { page ->
            page.visibility = if (page == selectedPage) View.VISIBLE else View.GONE
        }
    }

    private fun loginFirebaseUser() {
        val auth = firebaseAuth
        if (!firebaseReady || auth == null) {
            setStatusText(tvAuthStatus, "Firebase belum aktif. Tambahkan app/google-services.json dari Firebase Console.", R.color.warning)
            return
        }

        val email = authEmailInput.textValue()
        val password = authPasswordInput.textValue()
        if (email.isBlank() || password.isBlank()) {
            setStatusText(tvAuthStatus, "Isi email dan password Firebase terlebih dahulu.", R.color.warning)
            return
        }

        setStatusText(tvAuthStatus, "Mencoba login ke Firebase...", R.color.text_secondary)
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                authPasswordInput.text?.clear()
                updateAuthStatus()
                updateAuthGate()
                renderVaultEntries()
            }
            .addOnFailureListener { exception ->
                setStatusText(tvAuthStatus, "Login gagal: ${exception.localizedMessage}", R.color.danger)
                updateAuthGate()
            }
    }

    private fun registerFirebaseUser() {
        val auth = firebaseAuth
        if (!firebaseReady || auth == null) {
            setStatusText(tvAuthStatus, "Firebase belum aktif. Tambahkan app/google-services.json dari Firebase Console.", R.color.warning)
            return
        }

        val email = authEmailInput.textValue()
        val password = authPasswordInput.textValue()
        if (email.isBlank() || password.length < 6) {
            setStatusText(tvAuthStatus, "Email wajib diisi dan password Firebase minimal 6 karakter.", R.color.warning)
            return
        }

        setStatusText(tvAuthStatus, "Mendaftarkan akun Firebase...", R.color.text_secondary)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                authPasswordInput.text?.clear()
                updateAuthStatus()
                updateAuthGate()
                renderVaultEntries()
            }
            .addOnFailureListener { exception ->
                setStatusText(tvAuthStatus, "Daftar gagal: ${exception.localizedMessage}", R.color.danger)
                updateAuthGate()
            }
    }

    private fun signOutFirebaseUser() {
        firebaseAuth?.signOut()
        authPasswordInput.text?.clear()
        passwordInput.text?.clear()
        vaultPasswordInput.text?.clear()
        updateAuthStatus()
        resetDashboard()
        resetFeaturePages()
        updateAuthGate()
    }

    private fun updateAuthStatus() {
        if (!firebaseReady) {
            setStatusText(tvAuthStatus, "Firebase belum aktif. Tambahkan app/google-services.json, lalu rebuild aplikasi.", R.color.warning)
            return
        }

        val user = firebaseAuth?.currentUser
        if (user == null) {
            setStatusText(tvAuthStatus, "Firebase aktif. Login atau daftar agar data tersimpan ke Firestore.", R.color.text_secondary)
        } else {
            setStatusText(tvAuthStatus, "Login sebagai ${user.email ?: user.uid}. Data tersimpan ke Firestore.", R.color.success)
        }
    }

    private fun updateAuthGate() {
        val isLoggedIn = firebaseReady && firebaseAuth?.currentUser != null
        authGate.visibility = if (isLoggedIn) View.GONE else View.VISIBLE
        pageContainer.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
        bottomNavigation.visibility = if (isLoggedIn) View.VISIBLE else View.GONE

        if (isLoggedIn) {
            val selectedItemId = when (bottomNavigation.selectedItemId) {
                R.id.nav_vault,
                R.id.nav_document,
                R.id.nav_signature,
                R.id.nav_certificate -> bottomNavigation.selectedItemId
                else -> R.id.nav_home
            }
            if (bottomNavigation.selectedItemId != selectedItemId) {
                bottomNavigation.selectedItemId = selectedItemId
            } else {
                showPage(selectedItemId)
            }
        }
    }

    private fun currentUserCollection(collectionName: String, statusView: TextView): CollectionReference? {
        val database = firestore
        val user = firebaseAuth?.currentUser
        if (!firebaseReady || database == null) {
            setStatusText(statusView, "Firebase belum aktif. Tambahkan app/google-services.json dan rebuild aplikasi.", R.color.warning)
            return null
        }
        if (user == null) {
            setStatusText(statusView, "Login Firebase diperlukan agar data tersimpan ke Firestore.", R.color.warning)
            return null
        }

        return database.collection("users").document(user.uid).collection(collectionName)
    }

    private fun analyzePassword() {
        val password = passwordInput.textValue()
        if (password.isBlank()) {
            passwordInputLayout.error = "Password belum diisi"
            resetDashboard()
            return
        }

        passwordInputLayout.error = null
        val report = createPasswordReport(password)
        renderReport(report)
        savePasswordAnalysisToFirestore(report)
    }

    private fun saveVaultEntry() {
        val serviceName = vaultServiceInput.textValue()
        val username = vaultUsernameInput.textValue()
        val password = vaultPasswordInput.textValue()

        if (serviceName.isBlank() || username.isBlank() || password.isBlank()) {
            setStatusText(tvVaultSummary, "Lengkapi nama akun, username, dan password terlebih dahulu.", R.color.warning)
            return
        }

        val report = createPasswordReport(password)
        val fingerprint = sha256("VAULT|$serviceName|$username|$password")
        val userCollection = currentUserCollection("vault_entries", tvVaultSummary) ?: return
        val createdAt = System.currentTimeMillis()
        val entry = hashMapOf(
            "serviceName" to serviceName,
            "username" to username,
            "strengthLevel" to report.level,
            "strengthScore" to report.score,
            "fingerprintSha256" to fingerprint,
            "createdAt" to createdAt
        )

        setStatusText(tvVaultSummary, "Menyimpan ke Firestore...", R.color.text_secondary)
        userCollection.add(entry)
            .addOnSuccessListener {
                vaultPasswordInput.text?.clear()
                renderVaultEntries()
            }
            .addOnFailureListener { exception ->
                setStatusText(tvVaultSummary, "Gagal menyimpan ke Firestore: ${exception.localizedMessage}", R.color.danger)
            }
    }

    private fun clearVaultForm() {
        vaultServiceInput.text?.clear()
        vaultUsernameInput.text?.clear()
        vaultPasswordInput.text?.clear()
        renderVaultEntries()
    }

    private fun renderVaultEntries() {
        val userCollection = currentUserCollection("vault_entries", tvVaultSummary)
        if (userCollection == null) {
            tvVaultEntries.text = "Login Firebase diperlukan untuk membaca vault dari Firestore."
            return
        }

        setStatusText(tvVaultSummary, "Mengambil data vault dari Firestore...", R.color.text_secondary)
        userCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(MAX_VAULT_ENTRIES.toLong())
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    setStatusText(tvVaultSummary, "Belum ada akun tersimpan di Firestore.", R.color.text_secondary)
                    tvVaultEntries.text = "Daftar akun dari Firestore akan muncul di sini."
                    return@addOnSuccessListener
                }

                setStatusText(
                    tvVaultSummary,
                    "Tersimpan di Firestore: ${snapshot.size()} akun terbaru. Password asli tidak disimpan.",
                    R.color.success
                )
                tvVaultEntries.text = snapshot.documents.joinToString(separator = "\n\n") { document ->
                    val serviceName = document.getString("serviceName").orEmpty()
                    val username = document.getString("username").orEmpty()
                    val strengthLevel = document.getString("strengthLevel").orEmpty()
                    val strengthScore = document.getLong("strengthScore") ?: 0L
                    val createdAt = document.getLong("createdAt") ?: 0L
                    val fingerprint = document.getString("fingerprintSha256").orEmpty()
                    "$serviceName ($username)\nLevel: $strengthLevel - Skor: $strengthScore/100\nDibuat: ${formatTimestamp(createdAt)}\nFingerprint: ${fingerprint.take(28)}..."
                }
            }
            .addOnFailureListener { exception ->
                setStatusText(tvVaultSummary, "Gagal mengambil Firestore: ${exception.localizedMessage}", R.color.danger)
            }
    }

    private fun checkDocumentAuthenticity() {
        val documentName = documentNameInput.textValue().ifBlank { "Tanpa nama" }
        val documentContent = documentContentInput.textValue()
        val referenceHash = documentReferenceHashInput.textValue().replace(" ", "")

        if (documentContent.isBlank()) {
            setStatusText(tvDocumentStatus, "Status dokumen: isi dokumen belum diisi", R.color.warning)
            return
        }

        val documentHash = sha256("DOCUMENT|$documentName|$documentContent")
        val statusText: String
        val statusColor: Int
        val statusValue: String
        if (referenceHash.isBlank()) {
            statusText = "Status dokumen: hash awal berhasil dibuat"
            statusColor = R.color.primary
            statusValue = "Hash awal"
        } else if (documentHash.equals(referenceHash, ignoreCase = true)) {
            statusText = "Status dokumen: autentik, hash cocok"
            statusColor = R.color.success
            statusValue = "Autentik"
        } else {
            statusText = "Status dokumen: tidak cocok, ada perubahan data"
            statusColor = R.color.danger
            statusValue = "Tidak cocok"
        }

        setStatusText(tvDocumentStatus, statusText, statusColor)
        tvDocumentHash.text = "SHA-256 dokumen: $documentHash"
        tvDocumentDetails.text = "Nama dokumen: $documentName\nPanjang isi: ${documentContent.length} karakter\nMetadata ikut dihitung: nama dokumen + isi dokumen\nWaktu cek: ${currentTimeText()}\nFirestore: proses simpan..."
        saveDocumentCheckToFirestore(
            documentName = documentName,
            contentLength = documentContent.length,
            documentHash = documentHash,
            referenceHash = referenceHash.ifBlank { "-" },
            status = statusValue
        )
    }

    private fun generateDigitalSignature() {
        val signerName = signerNameInput.textValue()
        val documentContent = signatureDocumentInput.textValue()

        if (signerName.isBlank() || documentContent.isBlank()) {
            setStatusText(tvSignatureStatus, "Status signature: lengkapi nama dan isi dokumen", R.color.warning)
            return
        }

        val signature = createSignature(signerName, documentContent)
        val documentHash = sha256(documentContent)
        signatureVerifierInput.setText(signature)
        setStatusText(tvSignatureStatus, "Status signature: berhasil dibuat", R.color.success)
        tvGeneratedSignature.text = "Signature SHA-256: $signature"
        tvSignatureDetails.text = "Penandatangan: $signerName\nHash dokumen: $documentHash\nWaktu proses: ${currentTimeText()}\nFirestore: proses simpan..."
        saveSignatureRecordToFirestore(
            signerName = signerName,
            documentHash = documentHash,
            signatureHash = signature,
            action = "Generate",
            status = "Berhasil dibuat"
        )
    }

    private fun verifyDigitalSignature() {
        val signerName = signerNameInput.textValue()
        val documentContent = signatureDocumentInput.textValue()
        val submittedSignature = signatureVerifierInput.textValue().replace(" ", "")

        if (signerName.isBlank() || documentContent.isBlank() || submittedSignature.isBlank()) {
            setStatusText(tvSignatureStatus, "Status signature: data verifikasi belum lengkap", R.color.warning)
            return
        }

        val expectedSignature = createSignature(signerName, documentContent)
        val documentHash = sha256(documentContent)
        val isValid = expectedSignature.equals(submittedSignature, ignoreCase = true)
        if (isValid) {
            setStatusText(tvSignatureStatus, "Status signature: valid", R.color.success)
        } else {
            setStatusText(tvSignatureStatus, "Status signature: tidak valid", R.color.danger)
        }
        tvGeneratedSignature.text = "Signature SHA-256: $expectedSignature"
        tvSignatureDetails.text = "Signature diverifikasi dari nama penandatangan dan hash isi dokumen.\nWaktu verifikasi: ${currentTimeText()}\nFirestore: proses simpan..."
        saveSignatureRecordToFirestore(
            signerName = signerName,
            documentHash = documentHash,
            signatureHash = expectedSignature,
            action = "Verifikasi",
            status = if (isValid) "Valid" else "Tidak valid"
        )
    }

    private fun generateCertificateHashQr() {
        val participantName = certificateNameInput.textValue()
        val certificateId = certificateIdInput.textValue()
        val eventName = certificateEventInput.textValue()

        if (participantName.isBlank() || certificateId.isBlank() || eventName.isBlank()) {
            setStatusText(tvCertificateHash, "Hash sertifikat: lengkapi nama, ID, dan kegiatan", R.color.warning)
            return
        }

        val certificateHash = sha256("CERTIFICATE|$certificateId|$participantName|$eventName")
        val payload = "CERT_ID=$certificateId\nNAMA=$participantName\nKEGIATAN=$eventName\nHASH_SHA256=$certificateHash"
        certificateQrImage.setImageBitmap(createHashQrBitmap(payload))
        setStatusText(tvCertificateHash, "Hash sertifikat: $certificateHash", R.color.text_secondary)
        tvCertificatePayload.text = "Payload QR:\n$payload\n\nFirestore: proses simpan..."
        saveCertificateRecordToFirestore(
            certificateId = certificateId,
            participantName = participantName,
            eventName = eventName,
            certificateHash = certificateHash,
            payloadHash = sha256(payload)
        )
    }

    private fun resetFeaturePages() {
        renderVaultEntries()
        setStatusText(tvDocumentStatus, "Status dokumen: belum dicek", R.color.text_primary)
        tvDocumentHash.text = "SHA-256 dokumen: -"
        tvDocumentDetails.text = "Detail dokumen akan muncul di sini. Login Firebase diperlukan untuk menyimpan riwayat."
        setStatusText(tvSignatureStatus, "Status signature: belum dibuat", R.color.text_primary)
        tvGeneratedSignature.text = "Signature SHA-256: -"
        tvSignatureDetails.text = "Detail signature akan muncul di sini. Login Firebase diperlukan untuk menyimpan riwayat."
        certificateQrImage.setImageBitmap(createHashQrBitmap("SERTIFIKAT-DEMO"))
        setStatusText(tvCertificateHash, "Hash sertifikat: -", R.color.text_secondary)
        tvCertificatePayload.text = "Payload QR akan muncul di sini. Login Firebase diperlukan untuk menyimpan riwayat."
    }

    private fun savePasswordAnalysisToFirestore(report: PasswordReport) {
        val userCollection = currentUserCollection("password_analysis", tvAuthStatus) ?: run {
            tvAnalyzedAt.text = "Terakhir dianalisis: ${currentTimeText()} | Firestore: login diperlukan"
            return
        }
        val createdAt = System.currentTimeMillis()
        val data = hashMapOf(
            "passwordLength" to report.passwordLength,
            "strengthLevel" to report.level,
            "strengthScore" to report.score,
            "entropyBits" to report.entropyBits,
            "fingerprintSha256" to report.sha256Hash,
            "createdAt" to createdAt
        )

        userCollection.add(data)
            .addOnSuccessListener {
                tvAnalyzedAt.text = "Terakhir dianalisis: ${formatTimestamp(createdAt)} | Firestore: tersimpan"
            }
            .addOnFailureListener { exception ->
                tvAnalyzedAt.text = "Terakhir dianalisis: ${currentTimeText()} | Firestore gagal: ${exception.localizedMessage}"
            }
    }

    private fun saveDocumentCheckToFirestore(
        documentName: String,
        contentLength: Int,
        documentHash: String,
        referenceHash: String,
        status: String
    ) {
        val userCollection = currentUserCollection("document_checks", tvDocumentStatus) ?: run {
            tvDocumentDetails.text = "${tvDocumentDetails.text}\nFirestore: login diperlukan"
            return
        }
        val data = hashMapOf(
            "documentName" to documentName,
            "contentLength" to contentLength,
            "documentHash" to documentHash,
            "referenceHash" to referenceHash,
            "status" to status,
            "createdAt" to System.currentTimeMillis()
        )

        userCollection.add(data)
            .addOnSuccessListener {
                tvDocumentDetails.text = "${tvDocumentDetails.text}\nFirestore: tersimpan"
            }
            .addOnFailureListener { exception ->
                tvDocumentDetails.text = "${tvDocumentDetails.text}\nFirestore gagal: ${exception.localizedMessage}"
            }
    }

    private fun saveSignatureRecordToFirestore(
        signerName: String,
        documentHash: String,
        signatureHash: String,
        action: String,
        status: String
    ) {
        val userCollection = currentUserCollection("signature_records", tvSignatureStatus) ?: run {
            tvSignatureDetails.text = "${tvSignatureDetails.text}\nFirestore: login diperlukan"
            return
        }
        val data = hashMapOf(
            "signerName" to signerName,
            "documentHash" to documentHash,
            "signatureHash" to signatureHash,
            "action" to action,
            "status" to status,
            "createdAt" to System.currentTimeMillis()
        )

        userCollection.add(data)
            .addOnSuccessListener {
                tvSignatureDetails.text = "${tvSignatureDetails.text}\nFirestore: tersimpan"
            }
            .addOnFailureListener { exception ->
                tvSignatureDetails.text = "${tvSignatureDetails.text}\nFirestore gagal: ${exception.localizedMessage}"
            }
    }

    private fun saveCertificateRecordToFirestore(
        certificateId: String,
        participantName: String,
        eventName: String,
        certificateHash: String,
        payloadHash: String
    ) {
        val userCollection = currentUserCollection("certificate_records", tvCertificateHash) ?: run {
            tvCertificatePayload.text = "${tvCertificatePayload.text}\nFirestore: login diperlukan"
            return
        }
        val data = hashMapOf(
            "certificateId" to certificateId,
            "participantName" to participantName,
            "eventName" to eventName,
            "certificateHash" to certificateHash,
            "payloadHash" to payloadHash,
            "createdAt" to System.currentTimeMillis()
        )

        userCollection.add(data)
            .addOnSuccessListener {
                tvCertificatePayload.text = "${tvCertificatePayload.text}\nFirestore: tersimpan"
            }
            .addOnFailureListener { exception ->
                tvCertificatePayload.text = "${tvCertificatePayload.text}\nFirestore gagal: ${exception.localizedMessage}"
            }
    }

    private fun createPasswordReport(password: String): PasswordReport {
        val passwordLength = password.length
        val uppercaseCount = password.count { it.isUpperCase() }
        val lowercaseCount = password.count { it.isLowerCase() }
        val digitCount = password.count { it.isDigit() }
        val symbolCount = password.count { !it.isLetterOrDigit() }
        val uniqueCharacterCount = password.toSet().size

        val hasMinLength = passwordLength >= MINIMUM_RECOMMENDED_LENGTH
        val hasUppercase = uppercaseCount > 0
        val hasLowercase = lowercaseCount > 0
        val hasDigit = digitCount > 0
        val hasSymbol = symbolCount > 0
        val hasRepeatedCharacter = REPEATED_CHARACTER_PATTERN.containsMatchIn(password)
        val hasSequentialPattern = containsSequentialPattern(password)
        val hasCommonWord = containsCommonWord(password)
        val hasNoCommonPattern = !hasRepeatedCharacter && !hasSequentialPattern && !hasCommonWord

        val characterSetSize = calculateCharacterSetSize(
            hasUppercase = hasUppercase,
            hasLowercase = hasLowercase,
            hasDigit = hasDigit,
            hasSymbol = hasSymbol
        )
        val entropyBits = calculateEntropy(passwordLength, characterSetSize)
        val score = calculateStrengthScore(
            passwordLength = passwordLength,
            uniqueCharacterCount = uniqueCharacterCount,
            entropyBits = entropyBits,
            hasUppercase = hasUppercase,
            hasLowercase = hasLowercase,
            hasDigit = hasDigit,
            hasSymbol = hasSymbol,
            hasRepeatedCharacter = hasRepeatedCharacter,
            hasSequentialPattern = hasSequentialPattern,
            hasCommonWord = hasCommonWord
        )

        return PasswordReport(
            passwordLength = passwordLength,
            uppercaseCount = uppercaseCount,
            lowercaseCount = lowercaseCount,
            digitCount = digitCount,
            symbolCount = symbolCount,
            uniqueCharacterCount = uniqueCharacterCount,
            characterSetSize = characterSetSize,
            entropyBits = entropyBits,
            estimatedCrackTime = estimateCrackTime(entropyBits),
            sha256Hash = sha256(password),
            score = score,
            level = strengthLevel(score),
            feedback = createFeedback(
                hasMinLength = hasMinLength,
                hasUppercase = hasUppercase,
                hasLowercase = hasLowercase,
                hasDigit = hasDigit,
                hasSymbol = hasSymbol,
                hasRepeatedCharacter = hasRepeatedCharacter,
                hasSequentialPattern = hasSequentialPattern,
                hasCommonWord = hasCommonWord,
                score = score
            ),
            hasMinLength = hasMinLength,
            hasUppercase = hasUppercase,
            hasLowercase = hasLowercase,
            hasDigit = hasDigit,
            hasSymbol = hasSymbol,
            hasNoCommonPattern = hasNoCommonPattern
        )
    }

    private fun renderReport(report: PasswordReport) {
        val scoreColor = ContextCompat.getColor(this, colorForScore(report.score))
        progressStrength.setIndicatorColor(scoreColor)
        progressStrength.setProgress(report.score, true)

        tvStrengthLabel.text = report.level
        tvStrengthLabel.setTextColor(scoreColor)
        tvStrengthScore.text = "Skor: ${report.score}/100"
        tvFeedback.text = report.feedback
        tvAnalyzedAt.text = "Terakhir dianalisis: ${currentTimeText()} | Firestore: proses simpan jika sudah login"
        tvLengthValue.text = "Panjang password: ${report.passwordLength} karakter"
        tvCompositionValue.text = "Komposisi: huruf besar ${report.uppercaseCount}, huruf kecil ${report.lowercaseCount}, angka ${report.digitCount}, simbol ${report.symbolCount}"
        tvEntropyValue.text = "Entropy perkiraan: ${formatDecimal(report.entropyBits)} bit dari ${report.characterSetSize} kemungkinan karakter; karakter unik ${report.uniqueCharacterCount}"
        tvGuessTimeValue.text = "Estimasi ketahanan brute force: ${report.estimatedCrackTime}"
        tvHashValue.text = "SHA-256: ${report.sha256Hash}"

        setRuleText(tvRuleLength, "Minimal 12 karakter", report.hasMinLength)
        setRuleText(tvRuleUppercase, "Memiliki huruf besar", report.hasUppercase)
        setRuleText(tvRuleLowercase, "Memiliki huruf kecil", report.hasLowercase)
        setRuleText(tvRuleDigit, "Memiliki angka", report.hasDigit)
        setRuleText(tvRuleSymbol, "Memiliki simbol", report.hasSymbol)
        setRuleText(tvRulePattern, "Tidak memakai pola umum", report.hasNoCommonPattern)
    }

    private fun resetDashboard() {
        val neutralColor = ContextCompat.getColor(this, R.color.text_secondary)
        progressStrength.setIndicatorColor(ContextCompat.getColor(this, R.color.primary))
        progressStrength.setProgress(0, false)
        tvStrengthLabel.text = "Belum dianalisis"
        tvStrengthLabel.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
        tvStrengthScore.text = "Skor: 0/100"
        tvFeedback.text = "Masukkan password lalu tekan Analisis untuk melihat rekomendasi."
        tvAnalyzedAt.text = "Terakhir dianalisis: -"
        tvLengthValue.text = "Panjang password: 0 karakter"
        tvCompositionValue.text = "Komposisi: huruf besar 0, huruf kecil 0, angka 0, simbol 0"
        tvEntropyValue.text = "Entropy perkiraan: 0.0 bit"
        tvGuessTimeValue.text = "Estimasi ketahanan brute force: -"
        tvHashValue.text = "SHA-256: -"
        listOf(
            tvRuleLength to "Minimal 12 karakter",
            tvRuleUppercase to "Memiliki huruf besar",
            tvRuleLowercase to "Memiliki huruf kecil",
            tvRuleDigit to "Memiliki angka",
            tvRuleSymbol to "Memiliki simbol",
            tvRulePattern to "Tidak memakai pola umum"
        ).forEach { (view, label) ->
            view.text = "- $label"
            view.setTextColor(neutralColor)
        }
    }

    private fun calculateCharacterSetSize(
        hasUppercase: Boolean,
        hasLowercase: Boolean,
        hasDigit: Boolean,
        hasSymbol: Boolean
    ): Int {
        var characterSetSize = 0
        if (hasUppercase) characterSetSize += UPPERCASE_SET_SIZE
        if (hasLowercase) characterSetSize += LOWERCASE_SET_SIZE
        if (hasDigit) characterSetSize += DIGIT_SET_SIZE
        if (hasSymbol) characterSetSize += SYMBOL_SET_SIZE
        return characterSetSize
    }

    private fun calculateEntropy(passwordLength: Int, characterSetSize: Int): Double {
        if (passwordLength == 0 || characterSetSize == 0) return 0.0
        val logBaseTwo = ln(characterSetSize.toDouble()) / ln(2.0)
        return passwordLength * logBaseTwo
    }

    private fun calculateStrengthScore(
        passwordLength: Int,
        uniqueCharacterCount: Int,
        entropyBits: Double,
        hasUppercase: Boolean,
        hasLowercase: Boolean,
        hasDigit: Boolean,
        hasSymbol: Boolean,
        hasRepeatedCharacter: Boolean,
        hasSequentialPattern: Boolean,
        hasCommonWord: Boolean
    ): Int {
        val lengthScore = min(passwordLength * 4, 40)
        val varietyScore = listOf(hasUppercase, hasLowercase, hasDigit, hasSymbol).count { it } * 10
        val uniquenessScore = min(uniqueCharacterCount * 2, 12)
        val entropyBonus = when {
            entropyBits >= 80.0 -> 18
            entropyBits >= 60.0 -> 12
            entropyBits >= 40.0 -> 6
            else -> 0
        }
        val penalty = listOf(
            if (hasRepeatedCharacter) 12 else 0,
            if (hasSequentialPattern) 10 else 0,
            if (hasCommonWord) 18 else 0
        ).sum()

        return (lengthScore + varietyScore + uniquenessScore + entropyBonus - penalty).coerceIn(0, 100)
    }

    private fun containsSequentialPattern(password: String): Boolean {
        val normalizedPassword = password.lowercase(Locale.ROOT)
        if (normalizedPassword.length < 3) return false

        for (index in 0 until normalizedPassword.length - 2) {
            val firstCharacter = normalizedPassword[index]
            val secondCharacter = normalizedPassword[index + 1]
            val thirdCharacter = normalizedPassword[index + 2]
            val isLetterSequence = firstCharacter.isLetter() && secondCharacter.isLetter() && thirdCharacter.isLetter()
            val isDigitSequence = firstCharacter.isDigit() && secondCharacter.isDigit() && thirdCharacter.isDigit()
            if (!isLetterSequence && !isDigitSequence) continue

            val first = firstCharacter.code
            val second = secondCharacter.code
            val third = thirdCharacter.code
            if (second == first + 1 && third == second + 1) return true
            if (second == first - 1 && third == second - 1) return true
        }

        return KEYBOARD_PATTERNS.any { pattern ->
            normalizedPassword.contains(pattern)
        }
    }

    private fun containsCommonWord(password: String): Boolean {
        val normalizedPassword = password.lowercase(Locale.ROOT)
        return COMMON_PASSWORD_WORDS.any { word ->
            normalizedPassword.contains(word)
        }
    }

    private fun createFeedback(
        hasMinLength: Boolean,
        hasUppercase: Boolean,
        hasLowercase: Boolean,
        hasDigit: Boolean,
        hasSymbol: Boolean,
        hasRepeatedCharacter: Boolean,
        hasSequentialPattern: Boolean,
        hasCommonWord: Boolean,
        score: Int
    ): String {
        val suggestions = mutableListOf<String>()
        if (!hasMinLength) suggestions += "tambah panjang menjadi minimal 12 karakter"
        if (!hasUppercase) suggestions += "tambahkan huruf besar"
        if (!hasLowercase) suggestions += "tambahkan huruf kecil"
        if (!hasDigit) suggestions += "tambahkan angka"
        if (!hasSymbol) suggestions += "tambahkan simbol"
        if (hasRepeatedCharacter) suggestions += "hindari karakter berulang seperti aaa atau 111"
        if (hasSequentialPattern) suggestions += "hindari urutan seperti abc, 123, atau qwerty"
        if (hasCommonWord) suggestions += "hindari kata umum seperti password, admin, atau rahasia"

        return when {
            suggestions.isEmpty() && score >= 80 -> "Password sudah kuat untuk contoh aplikasi ini. Tetap gunakan password berbeda untuk setiap akun."
            suggestions.isEmpty() -> "Struktur password sudah memenuhi checklist dasar, tetapi frasa yang lebih panjang akan menaikkan entropy."
            else -> "Saran: ${suggestions.joinToString(separator = "; ")}."
        }
    }

    private fun estimateCrackTime(entropyBits: Double): String {
        if (entropyBits <= 0.0) return "-"
        if (entropyBits >= 90.0) return "> 1 juta tahun"

        val combinations = 2.0.pow(entropyBits)
        val averageSeconds = combinations / (2 * GUESSES_PER_SECOND)
        return when {
            averageSeconds < 1 -> "kurang dari 1 detik"
            averageSeconds < 60 -> "${averageSeconds.toInt()} detik"
            averageSeconds < 3600 -> "${(averageSeconds / 60).toInt()} menit"
            averageSeconds < 86400 -> "${(averageSeconds / 3600).toInt()} jam"
            averageSeconds < 31536000 -> "${(averageSeconds / 86400).toInt()} hari"
            averageSeconds < 3153600000 -> "${(averageSeconds / 31536000).toInt()} tahun"
            else -> "> 100 tahun"
        }
    }

    private fun createSignature(signerName: String, documentContent: String): String {
        val documentHash = sha256(documentContent)
        return sha256("SIGNATURE|$signerName|$documentHash")
    }

    private fun createHashQrBitmap(seed: String): Bitmap {
        val moduleCount = 29
        val quietZone = 2
        val scale = 8
        val bitmapSize = (moduleCount + quietZone * 2) * scale
        val bitmap = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            isAntiAlias = false
            style = Paint.Style.FILL
        }
        val modules = Array(moduleCount) { BooleanArray(moduleCount) }

        canvas.drawColor(Color.WHITE)
        addFinderPattern(modules, 0, 0)
        addFinderPattern(modules, moduleCount - 7, 0)
        addFinderPattern(modules, 0, moduleCount - 7)

        val hashBytes = MessageDigest.getInstance("SHA-256").digest(seed.toByteArray(Charsets.UTF_8))
        for (y in 0 until moduleCount) {
            for (x in 0 until moduleCount) {
                if (isFinderPatternArea(x, y, moduleCount)) continue

                val bitIndex = (y * moduleCount + x) % (hashBytes.size * 8)
                val byteValue = hashBytes[bitIndex / 8].toInt() and 0xff
                val rawBit = ((byteValue shr (bitIndex % 8)) and 1) == 1
                val mixedBit = (x * 31 + y * 17 + seed.length) % 7 == 0
                modules[y][x] = rawBit != mixedBit
            }
        }

        paint.color = Color.BLACK
        for (y in 0 until moduleCount) {
            for (x in 0 until moduleCount) {
                if (modules[y][x]) {
                    val left = (x + quietZone) * scale
                    val top = (y + quietZone) * scale
                    canvas.drawRect(
                        left.toFloat(),
                        top.toFloat(),
                        (left + scale).toFloat(),
                        (top + scale).toFloat(),
                        paint
                    )
                }
            }
        }

        return bitmap
    }

    private fun addFinderPattern(modules: Array<BooleanArray>, startX: Int, startY: Int) {
        for (y in 0 until 7) {
            for (x in 0 until 7) {
                val isBorder = x == 0 || x == 6 || y == 0 || y == 6
                val isCenter = x in 2..4 && y in 2..4
                modules[startY + y][startX + x] = isBorder || isCenter
            }
        }
    }

    private fun isFinderPatternArea(x: Int, y: Int, moduleCount: Int): Boolean {
        val inTopLeft = x in 0..6 && y in 0..6
        val inTopRight = x in moduleCount - 7 until moduleCount && y in 0..6
        val inBottomLeft = x in 0..6 && y in moduleCount - 7 until moduleCount
        return inTopLeft || inTopRight || inBottomLeft
    }

    private fun sha256(text: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(text.toByteArray(Charsets.UTF_8))
        return digest.joinToString(separator = "") { byte ->
            String.format(Locale.US, "%02x", byte.toInt() and 0xff)
        }
    }

    private fun strengthLevel(score: Int): String {
        return when {
            score < 40 -> "Lemah"
            score < 70 -> "Sedang"
            score < 90 -> "Kuat"
            else -> "Sangat kuat"
        }
    }

    private fun colorForScore(score: Int): Int {
        return when {
            score < 40 -> R.color.danger
            score < 70 -> R.color.warning
            else -> R.color.success
        }
    }

    private fun setRuleText(view: TextView, label: String, isPassed: Boolean) {
        val colorRes = if (isPassed) R.color.success else R.color.warning
        val prefix = if (isPassed) "[OK]" else "[!]"
        view.text = "$prefix $label"
        view.setTextColor(ContextCompat.getColor(this, colorRes))
    }

    private fun setStatusText(view: TextView, text: String, colorRes: Int) {
        view.text = text
        view.setTextColor(ContextCompat.getColor(this, colorRes))
    }

    private fun TextInputEditText.textValue(): String {
        return text?.toString()?.trim().orEmpty()
    }

    private fun currentTimeText(): String {
        val localeIndonesia = Locale.forLanguageTag("id-ID")
        return SimpleDateFormat("HH:mm:ss", localeIndonesia).format(Date())
    }

    private fun formatDecimal(value: Double): String {
        return String.format(Locale.US, "%.1f", value)
    }

    private fun formatTimestamp(timestamp: Long): String {
        val localeIndonesia = Locale.forLanguageTag("id-ID")
        return SimpleDateFormat("dd MMM yyyy, HH:mm", localeIndonesia).format(Date(timestamp))
    }

    private data class PasswordReport(
        val passwordLength: Int,
        val uppercaseCount: Int,
        val lowercaseCount: Int,
        val digitCount: Int,
        val symbolCount: Int,
        val uniqueCharacterCount: Int,
        val characterSetSize: Int,
        val entropyBits: Double,
        val estimatedCrackTime: String,
        val sha256Hash: String,
        val score: Int,
        val level: String,
        val feedback: String,
        val hasMinLength: Boolean,
        val hasUppercase: Boolean,
        val hasLowercase: Boolean,
        val hasDigit: Boolean,
        val hasSymbol: Boolean,
        val hasNoCommonPattern: Boolean
    )

    private companion object {
        const val MINIMUM_RECOMMENDED_LENGTH = 12
        const val UPPERCASE_SET_SIZE = 26
        const val LOWERCASE_SET_SIZE = 26
        const val DIGIT_SET_SIZE = 10
        const val SYMBOL_SET_SIZE = 33
        const val GUESSES_PER_SECOND = 1_000_000_000.0
        const val MAX_VAULT_ENTRIES = 5

        val REPEATED_CHARACTER_PATTERN = Regex("(.)\\1{2,}")
        val KEYBOARD_PATTERNS = listOf("qwerty", "asdf", "zxcv", "1234", "abcd", "9876")
        val COMMON_PASSWORD_WORDS = listOf(
            "password",
            "admin",
            "rahasia",
            "sandi",
            "indonesia",
            "bismillah",
            "welcome",
            "iloveyou",
            "letmein"
        )
    }
}
