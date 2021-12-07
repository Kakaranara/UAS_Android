package umn.ac.id.uasmobileapp;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class AdminBarangFragment extends Fragment implements View.OnClickListener{
    private View view;
    private RecyclerView recyclerView;
    //AdminProductAdapter adapter;
    DatabaseReference mbase;
    DatabaseReference imageRoot = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference("products");
    private String currentUser = "BU00001";
    public Button button;
    public String user_key, key;
    Session session;
    private FirebaseRecyclerAdapter<Product, AdminProductViewholder> adapter; // Create Object of the Adapter class

    private Uri imageUri, imageUri2;
    private StorageReference mStorageRef;

    FirebaseDatabase rootNode;
    DatabaseReference reference,secRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminBarangFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    /*public static AdminBarangFragment newInstance(String param1, String param2) {
        AdminBarangFragment fragment = new AdminBarangFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            key = bundle.getString("businessId", "");
        }
        System.out.println("----------------------------- KEY : " + key);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_barang, container, false);
        button = view.findViewById(R.id.manage_k);
        button.setOnClickListener(this);

        session = new Session(getContext());
        user_key = session.getKey();

        key = AdminActivity.businessId;


        mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("products");
        mStorageRef = FirebaseStorage.getInstance("gs://final-project-mobile-app-98d46.appspot.com").getReference();

        recyclerView = (RecyclerView) view.findViewById(R.id.rvProduct);

        // To display the Recycler view linearly
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);

        // Connecting Adapter class with the Recycler view*/
        return view;
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    public void onStart()
    {
        super.onStart();
        // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
        System.out.println(key);

        FirebaseRecyclerOptions<Product> options
                = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mbase.orderByChild("business_id").equalTo(key), Product.class)
                .build();

        //System.out.println(key);

        adapter = new FirebaseRecyclerAdapter<Product, AdminProductViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminProductViewholder holder, int position, @NonNull Product model) {
                        //DatabaseReference getBusinessId = getRef(position).child("business_id").getRef();
                        //final String product_key = getRef(position).getKey();
                        //Query query = mbase.orderByChild("business_id").equalTo(key);

                        Log.d("FIREBASE Admin Barang", String.valueOf(getRef(position).getKey()));

                        final String product_key = getRef(position).getKey();
                        System.out.println(product_key);
                        holder.deleteBtn.setText(product_key);
                        holder.editBtn.setText(product_key);

                        //Get & Show Name
                        DatabaseReference getName = getRef(position).child("product_name").getRef();
                        getName.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    String name = dataSnapshot.getValue().toString();
                                    //System.out.println("------------LINK-------------:" + name);
                                    holder.product_name.setText(name);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        //Get & Show Image
                        DatabaseReference getImage = getRef(position).child("picture_path").getRef();
                        getImage.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    String image = dataSnapshot.getValue().toString();
                                    //System.out.println("------------LINK-------------:" + image);
                                    Picasso.get().load(image).placeholder(R.mipmap.ic_launcher).error(R.drawable.basket_white).into(holder.product_image);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        DatabaseReference getPrice = getRef(position).child("product_price").getRef();
                        getPrice.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String price = dataSnapshot.getValue().toString();
                                    holder.product_price.setText(price);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        DatabaseReference getStock = getRef(position).child("stock").getRef();
                        getStock.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String stock = dataSnapshot.getValue().toString();
                                    holder.product_quantity.setText(stock);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

//                        if(business_id.equals(key)){
//
//
////                                        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
////                                            @Override
////                                            public void onClick(View view) {
////                                                mbase.child((String) holder.deleteBtn.getText()).removeValue();
////                                            }
////                                        });
////
////                                        //holder.product_quantity.setText(model.getStock());
////                                        holder.editBtn.setOnClickListener(new View.OnClickListener() {
////                                            @Override
////                                            public void onClick(View view) {
////                                                showEditDialog();
////                                            }
////                                        });
//
//                        }
                    }

                    @NonNull
                    @Override
                    public AdminProductViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_admin, parent, false);
                        return new AdminProductViewholder(view);
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onClick(View view) {
        System.out.println(view);
        showCustomDialog();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    void showCustomDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.add_product_admin);
        final EditText nameEt = dialog.findViewById(R.id.name_fill);
        final EditText priceEt = dialog.findViewById(R.id.price_fill);
        final EditText discountEt = dialog.findViewById(R.id.discount_fill);
        final EditText stockEt = dialog.findViewById(R.id.stock_fill);
        final EditText descEt = dialog.findViewById(R.id.desc_fill);
        Button submit = dialog.findViewById(R.id.submit);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button upload = dialog.findViewById(R.id.change_picture);
        Button camera = dialog.findViewById(R.id.camera);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Tombol Submit di Klik");
                rootNode = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/");
                reference = rootNode.getReference("products");
                //String key = reference.getKey();

                String product_name = nameEt.getText().toString();
                String etPrice= priceEt.getText().toString();
                int price = Integer.parseInt(etPrice);
                String etDiscount = discountEt.getText().toString();
                int discount = Integer.parseInt(etDiscount);
                String etStock = stockEt.getText().toString();
                int stock = Integer.parseInt(etStock);
                String description = descEt.getText().toString();
                String id = "PR000" + etStock;

                //Insert Data to Database
                ProductHelperClass helperClass = new ProductHelperClass(key, null, description, product_name, price, stock, discount);
                reference.child("PR000" + etStock).setValue(helperClass);

                if(imageUri != null){
                    uploadToFirebase(imageUri, id);
                }else{
                    Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT);
                }

                dialog.dismiss();

                AdminBarangFragment fragment = new AdminBarangFragment();
                getFragmentManager().beginTransaction().replace(R.id.adminContainerFragment, fragment).commit();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent();
                takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        dialog.show();
    }

    private void uploadToFirebase(Uri uri, String id){
        StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        //System.out.println("----------URI--------------- Luar : "+uri);
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //System.out.println("----------ID--------------- Tengah : "+id);
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //System.out.println("----------ID--------------- Dalam : "+id);
                        //Image model = new Image(uri.toString());
                        String modelId = mbase.push().getKey();
                        imageRoot.child(id).child("picture_path").setValue(uri.toString());
                        //Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == AdminActivity.RESULT_OK && data != null){
            System.out.println("Masuk");
            imageUri = data.getData();
        }

        if(requestCode == REQUEST_IMAGE_CAPTURE){
            System.out.println("Masuk Camera");
            imageUri = data.getData();
        }
    }

    public void showEditDialog(String product_id, int position){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.edit_list_product);
        final EditText nameEt = dialog.findViewById(R.id.name_fill);
        final EditText priceEt = dialog.findViewById(R.id.price_fill);
        final EditText discountEt = dialog.findViewById(R.id.discount_fill);
        final EditText stockEt = dialog.findViewById(R.id.stock_fill);
        final EditText descEt = dialog.findViewById(R.id.desc_fill);
        Button submit = dialog.findViewById(R.id.submit);
        Button cancel = dialog.findViewById(R.id.cancel);
        Button upload = dialog.findViewById(R.id.change_picture);

        FirebaseDatabase nodePos = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/");
        secRef = nodePos.getReference("products").child(product_id);

        secRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    nameEt.setText(dataSnapshot.child("product_name").getValue().toString());
                    priceEt.setText(dataSnapshot.child("price").getValue().toString());
                    discountEt.setText(dataSnapshot.child("discount").getValue().toString());
                    stockEt.setText(dataSnapshot.child("stock").getValue().toString());
                    descEt.setText(dataSnapshot.child("description").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEt.getText().toString();
                String etPrice = priceEt.getText().toString();
                int price = Integer.parseInt(etPrice);
                String etDiscount = discountEt.getText().toString();
                int discount = Integer.parseInt(etDiscount);
                String etStock = stockEt.getText().toString();
                int stock = Integer.parseInt(etStock);
                String desc = descEt.getText().toString();

                //Edit Data to Database
                //reference.child("product_name").setValue(name);
                //Insert Data to Database

                if(imageUri != null){
                    uploadToFirebase(imageUri, product_id);
                }else{
                    Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT);
                }

                ProductHelperClass helperClass = new ProductHelperClass(key, null, desc, name, price, stock, discount);
                secRef.setValue(helperClass);

                if(imageUri != null){
                    uploadToFirebase(imageUri, product_id);
                }else{
                    Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT);
                }

                dialog.dismiss();

//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.adminContainerFragment, new AdminBarangFragment(),null);
//                transaction.commit();

                AdminBarangFragment fragment = new AdminBarangFragment();
                getFragmentManager().beginTransaction().replace(R.id.adminContainerFragment, fragment).commit();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public class AdminProductViewholder extends RecyclerView.ViewHolder {
        private final DatabaseReference mbase;
        TextView product_name, product_price, product_quantity;
        ShapeableImageView product_image;
        Button editBtn, deleteBtn;
        public AdminProductViewholder(@NonNull View itemView)
        {
            super(itemView);
            mbase = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference().child("products");
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_quantity = itemView.findViewById(R.id.prduct_quantity);
            product_image = itemView.findViewById(R.id.product_image);
            editBtn = itemView.findViewById(R.id.editBtn);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditDialog(editBtn.getText().toString(), getAdapterPosition());
                }
            });

            deleteBtn = itemView.findViewById(R.id.delete_product);
            itemView.findViewById(R.id.delete_product).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mbase.child((String) deleteBtn.getText()).removeValue();
                }
            });
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}