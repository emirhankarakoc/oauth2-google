export default function Footer() {
  return (
    <div className="bg-black grid grid-cols-4 place-items-center p-20">
      <div className="col-span-2">
        <div className="font-oswald text-5xl text-white">KARAKOC</div>
        <div className="text-gray-300">
          Lorem ipsum dolor sit amet consectetur adipisicing elit. Possimus unde
          nihil obcaecati? Qui molestiae nulla ad dolore, ut impedit sunt optio
          autem voluptates vitae explicabo quaerat quas! Beatae, reiciendis
          nihil.
        </div>
        <div className="flex flex-row gap-20 text-white mt-10">
          <i className="fa-brands fa-twitter"></i>
          <i className="fa-brands fa-facebook"></i>{" "}
          <i className="fa-brands fa-tiktok"></i>
          <i className="fa-brands fa-instagram"></i>
        </div>
      </div>
      <div className="col-span-1">
        <div className="font-sfpro text-xl text-white mb-3">Services</div>
        <p className="text-gray-300">Bonus program</p>
        <p className="text-gray-300">Gift cards</p>
        <p className="text-gray-300">Credit and payment</p>
        <p className="text-gray-300">Service contracts</p>
        <p className="text-gray-300">Non-cash account</p>
        <p className="text-gray-300">Payment</p>
      </div>
      <div className="col-span-1">
        <div className="font-sfpro text-xl text-white mb-3">
          Assistance to the buyer
        </div>
        <p className="text-gray-300">Find an order</p>
        <p className="text-gray-300">Terms of delivery</p>
        <p className="text-gray-300">Exchange and return of goods</p>
        <p className="text-gray-300">Guarantee</p>
        <p className="text-gray-300">Frequently asked questions</p>
        <p className="text-gray-300">Terms of use of the site</p>
      </div>
    </div>
  );
}
